package com.redhat.examples.gateway;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GatewayTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CamelContext camelContext;

    @Test
    public void testSwaggerEndpoint() {
 
        ResponseEntity<String> orderResponse = restTemplate.getForEntity("/api/api-doc", String.class);
        assertThat(orderResponse.getStatusCodeValue(), equalTo(HttpStatus.SC_OK));
    }

    @Test
    @DirtiesContext
    public void activeDefinitionTest() throws Exception {
    	
    	String definition = "a particular process or method for trying or assessing.";
    	String responseStr = "{ \"input\": \"test\", \"definition\": \"" + definition +"\" }";
  
        NotifyBuilder notify = new NotifyBuilder(camelContext)
            .fromRoute("definition-word")
            .whenDone(1)
            .create();
    	
    	camelContext.getRouteDefinition("definition-word").adviceWith(camelContext, new AdviceWithRouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				interceptSendToEndpoint("http4*").skipSendToOriginalEndpoint().setBody(constant(responseStr));
			}
		});

        ResponseEntity<String> orderResponse = restTemplate.getForEntity("/api/definition/test", String.class);
        assertThat(orderResponse.getStatusCodeValue(), equalTo(HttpStatus.SC_OK));
        
        assertThat(notify.matches(10, TimeUnit.SECONDS), Matchers.is(true));

        JsonNode node = new ObjectMapper(new JsonFactory()).readTree(orderResponse.getBody());
        assertThat("test", equalTo(node.get("input").asText()));
        assertThat(definition, equalTo(node.get("definition").asText()));
        
    }
    
    @Test
    @DirtiesContext
    public void inactiveDefinitionTest() throws Exception {
    	
  
        NotifyBuilder notify = new NotifyBuilder(camelContext)
            .fromRoute("definition-word")
            .whenDone(1)
            .create();
   	
    	camelContext.getRouteDefinition("definition-word").adviceWith(camelContext, new AdviceWithRouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				interceptSendToEndpoint("http*").skipSendToOriginalEndpoint().throwException(new IllegalArgumentException("Forced"));
			}
		});

        ResponseEntity<String> orderResponse = restTemplate.getForEntity("/api/definition/test", String.class);
        assertThat(orderResponse.getStatusCodeValue(), equalTo(HttpStatus.SC_SERVICE_UNAVAILABLE));
        
        assertThat(notify.matches(10, TimeUnit.SECONDS), Matchers.is(true));

        JsonNode node = new ObjectMapper(new JsonFactory()).readTree(orderResponse.getBody());
        assertThat("test", equalTo(node.get("input").asText()));
        
    }

    
}
