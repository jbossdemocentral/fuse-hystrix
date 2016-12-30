package com.redhat.examples.definition.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import com.redhat.examples.definition.DefinitionService;

@Component
public class DefinitionRoutes extends RouteBuilder {

	@Override
	public void configure() throws Exception {
        restConfiguration()
        .contextPath("/api").apiContextPath("/api-doc")
            .apiProperty("api.title", "Definition REST API")
            .apiProperty("api.version", "1.0")
            .apiProperty("cors", "true")
            .apiProperty("host", "")
            .apiContextRouteId("doc-api")
        .component("servlet")
        .dataFormatProperty("include", "NON_NULL")
        .bindingMode(RestBindingMode.json);
        
        rest("/definition").description("Definition REST service")
        .get("/{word}").description("Definiton lookup by word")
            .route().routeId("definition-lookup")
            .setBody().simple("${header.word}")
            .bean(DefinitionService.class, "lookup");
		
	}

}
