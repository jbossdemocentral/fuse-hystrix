package com.redhat.examples.definition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {


/*
    @Autowired
    private CamelContext camelContext;
*/
	
    @Test
    public void newOrderTest() {
    	/*
        // Wait for maximum 5s until the first order gets inserted and processed
        NotifyBuilder notify = new NotifyBuilder(camelContext)
            .fromRoute("generate-order")
            .whenDone(2)
            .and()
            .fromRoute("process-order")
            .whenDone(1)
            .create();
        assertThat(notify.matches(10, TimeUnit.SECONDS)).isTrue();

        // Then call the REST API
        ResponseEntity<Order> orderResponse = restTemplate.getForEntity("/camel-rest-sql/books/order/1", Order.class);
        assertThat(orderResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Order order = orderResponse.getBody();
        assertThat(order.getId()).isEqualTo(1);
        assertThat(order.getAmount()).isBetween(1, 10);
        assertThat(order.getItem()).isIn("Camel", "ActiveMQ");
        assertThat(order.getDescription()).isIn("Camel in Action", "ActiveMQ in Action");
        assertThat(order.isProcessed()).isTrue();

        ResponseEntity<List<Book>> booksResponse = restTemplate.exchange("/camel-rest-sql/books",
            HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>(){});
        assertThat(booksResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Book> books = booksResponse.getBody();
        assertThat(books).hasSize(2);
        assertThat(books).element(0)
            .hasFieldOrPropertyWithValue("description", "ActiveMQ in Action");
        assertThat(books).element(1)
            .hasFieldOrPropertyWithValue("description", "Camel in Action");
            */
    }
}
