package com.project.microservices.product_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import static org.hamcrest.Matchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer=new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI="http://localhost";
		RestAssured.port=port;
	}

	static {
		mongoDBContainer.start();
	}
	@Test
	void shouldCreateProduct()
	{
		String requestBody= """
				{
				  "name": "Mechanical Keyboard",
				  "description": "RGB backlit mechanical keyboard with blue switches.",
				  "price": 79.99
				}
				""";//java 14

		RestAssured
				.given()
					.contentType("application/json")
					.body(requestBody)
				.when()
					.post("/api/product")
				.then()
					.statusCode(201)
					.body("id",notNullValue())
					.body("name",equalTo("Mechanical Keyboard"))
					.body("description",equalTo("RGB backlit mechanical keyboard with blue switches."))
					.body("price",equalTo(79.99f));
	}

}
