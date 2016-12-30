package com.redhat.examples.definition;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DefinitionTest {


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
	    public void activeDefinitionTest() throws Exception {
	    	
	    	String definition = "Occurring, found, or done often";
	  
	        NotifyBuilder notify = new NotifyBuilder(camelContext)
	            .fromRoute("definition-lookup")
	            .whenDone(1)
	            .create();

	        ResponseEntity<String> orderResponse = restTemplate.getForEntity("/api/definition/common", String.class);
	        assertThat(orderResponse.getStatusCodeValue(), equalTo(HttpStatus.SC_OK));
	        
	        assertThat(notify.matches(10, TimeUnit.SECONDS), Matchers.is(true));

	        JsonNode node = new ObjectMapper(new JsonFactory()).readTree(orderResponse.getBody());
	        assertThat("common", equalTo(node.get("input").asText()));
	        assertThat(definition, equalTo(node.get("definition").asText()));
	        
	    }
}
