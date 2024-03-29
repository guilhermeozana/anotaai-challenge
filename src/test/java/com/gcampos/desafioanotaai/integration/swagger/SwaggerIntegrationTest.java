package com.gcampos.desafioanotaai.integration.swagger;

import com.gcampos.desafioanotaai.integration.testcontainer.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {
    @Value("${server.port}")
    int serverPort = 8888;

    @Test
    @DisplayName("GivenBaseSwaggerPath_whenGetRequest_thenReturnSwaggerPage")
    void integrationTestGivenBaseSwaggerPath_whenGetRequest_thenReturnSwaggerPage() {


        var content = given()
                .basePath("/swagger-ui/index.html")
                .port(serverPort)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("Swagger UI"));
    }

}
