package com.redhat.examples.gateway.routes;

import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.redhat.examples.gateway.Result;

@Component
public class GatewayRoutes extends RouteBuilder {
	
	@Value("${hystrix.executionTimeout}")
	private int hystrixExecutionTimeout;
	
	@Value("${hystrix.groupKey}")
	private String hystrixGroupKey;
	
	@Value("${hystrix.circuitBreakerEnabled}")
	private boolean hystrixCircuitBreakerEnabled;

	@Override
	public void configure() throws Exception {
	
        restConfiguration()
        .contextPath("/api").apiContextPath("/api-doc")
            .apiProperty("api.title", "Fuse Integration Services Hystrix Demo REST API")
            .apiProperty("host", "")
            .apiProperty("api.version", "1.0")
            .apiProperty("cors", "true")
            .apiContextRouteId("doc-api")
        .component("servlet")
        .dataFormatProperty("include", "NON_NULL");

        rest("/definition").description("Provides Definition Services")

        .get("/{word}").description("Get meaning for word")
            .route().routeId("definition-word")
            	.hystrix()
            		.hystrixConfiguration()
            			.executionTimeoutInMilliseconds(hystrixExecutionTimeout)
            			.groupKey(hystrixGroupKey)
            			.circuitBreakerEnabled(hystrixCircuitBreakerEnabled)
            		.end()
            		.removeHeaders("Camel*")
            		.setHeader("CamelHttpMethod", constant("GET"))
            		.recipientList(simple("http4://{{definition.hostname}}:{{definition.portnumber}}/api/definition/${header.word}?bridgeEndpoint=true")).end()
            		.onFallback()
            			.log("Failed to Query Definition Service")
            			.to("direct:default-response")
            			.marshal().json(JsonLibrary.Jackson)
            			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.SERVICE_UNAVAILABLE.getStatusCode()))
            			.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            		.end();

        // Provide a response
        from("direct:default-response").routeId("default-response").description("Default API response")
        .process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				Result result = new Result();
				result.setInput(exchange.getIn().getHeader("word", String.class));
				exchange.getIn().setBody(result);
			}
        	
        });
        
	}

}
