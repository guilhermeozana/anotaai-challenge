package com.gcampos.desafioanotaai.integration.controller;

import com.gcampos.desafioanotaai.domain.dto.ProductDTO;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.domain.model.Product;
import com.gcampos.desafioanotaai.integration.testcontainer.AbstractIntegrationTest;
import com.gcampos.desafioanotaai.util.CategoryCreator;
import com.gcampos.desafioanotaai.util.ProductCreator;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
        import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProductIntegrationControllerTest extends AbstractIntegrationTest {

    static int serverPort = 8888;

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static ProductDTO productDTO;
    private static String productId;

    private static Category category;

    @BeforeAll
    public static void setup() throws IOException {
        //Given
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    static void createProductDTOWithExistentCategory() throws IOException {
        specification = new RequestSpecBuilder()
                .setBasePath("/api/categories")
                .setPort(serverPort)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var categoryContent = given()
                .spec(specification)
                .contentType("application/json")
                .body(CategoryCreator.buildCategoryDTO())
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        category = objectMapper.readValue(categoryContent, Category.class);

        productDTO = ProductCreator.buildProductDTOWithExistentCategory(category.getId());

        specification.basePath("/api/products");
    }

    @Test
    @Order(1)
    @DisplayName("Given Product object when create then returns Product object")
    void integrationTest_givenProductObject_whenCreate_ThenReturnsProductObject() throws IOException {
        createProductDTOWithExistentCategory();

        var productContent = given()
                .spec(specification)
                .contentType("application/json")
                .body(productDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var createdProduct = objectMapper.readValue(productContent, Product.class);

        BeanUtils.copyProperties(createdProduct, productDTO);
        productId = createdProduct.getId();


        assertNotNull(createdProduct);
        assertFalse(createdProduct.getId().isEmpty());
        assertEquals("Test", createdProduct.getTitle());
        assertEquals("Product Test", createdProduct.getDescription());
        assertEquals("123", createdProduct.getOwnerId());
    }

    @Test
    @Order(2)
    @DisplayName("Given Product object when getAll then returns Product list")
    void integrationTest_givenProductObject_whenGetAll_ThenReturnsProductObject() throws IOException {

        var content = given()
                .spec(specification)
                .contentType("application/json")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Product> products = objectMapper.readValue(content, new TypeReference<List<Product>>(){});

        assertNotNull(content);
        assertFalse(content.isEmpty());
        assertEquals("Test", products.get(0).getTitle());
        assertEquals("Product Test", products.get(0).getDescription());
        assertEquals("123", products.get(0).getOwnerId());
    }

    @Test
    @Order(3)
    @DisplayName("Given Product Object when findAll then Returns Product List")
    void integrationTest_givenProductObject_whenFindAll_thenReturnsProductList() throws IOException {

        var content = given().spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Product[] productArray = objectMapper.readValue(content, Product[].class);
        List<Product> productList = Arrays.asList(productArray);

        Product foundProduct = productList.get(0);

        assertNotNull(foundProduct);
        assertNotNull(foundProduct.getId());
        assertEquals("Test", foundProduct.getTitle());
    }

    @Test
    @Order(4)
    @DisplayName("Given Product Object when Update then Returns Updated Product Object")
    void integrationTest_givenProductObject_whenUpdate_thenReturnsUpdatedProductObject() throws IOException {

        var content = given()
                .spec(specification)
                .contentType("application/json")
                .body(productDTO)
                .when()
                .put("/" + productId)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Product updatedProduct = objectMapper.readValue(content, Product.class);

        BeanUtils.copyProperties(updatedProduct, productDTO);
        productId = updatedProduct.getId();

        assertNotNull(updatedProduct);
        assertNotNull(updatedProduct.getId());
        assertEquals("Test", updatedProduct.getTitle());
    }

    @Test
    @Order(5)
    @DisplayName("Given Product object when delete then returns no content")
    void integrationTest_givenProductObject_whenDelete_thenReturnsNoContent() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .pathParam("id", productId)
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    @DisplayName("Given unexistent Product object when update then throws ProductNotFoundException")
    void integrationTest_givenUnexistentProductObject_whenUpdate_thenThrowsProductNotFoundException() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .contentType("application/json")
                .body(productDTO)
                .when()
                .put("/" + productId)
                .then()
                .statusCode(404);
    }

    @Test
    @Order(7)
    @DisplayName("Given unexistent Product object when delete then throws ProductNotFoundException")
    void integrationTest_givenUnexistentProductObject_whenDelete_thenThrowsProductNotFoundException() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .pathParam("id", productId)
                .when()
                .delete("{id}")
                .then()
                .statusCode(404);
    }

}
