package com.gcampos.desafioanotaai.integration.controller;

import com.gcampos.desafioanotaai.domain.dto.CategoryDTO;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.integration.testcontainer.AbstractIntegrationTest;
import com.gcampos.desafioanotaai.util.CategoryCreator;
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
public class CategoryIntegrationControllerTest extends AbstractIntegrationTest {

    static int serverPort = 8888;

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static CategoryDTO categoryDTO;
    private static String categoryId;

    @BeforeAll
    public static void setup() {
        //Given
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/api/categories")
                .setPort(serverPort)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        
        categoryDTO = CategoryCreator.buildCategoryDTO();
    }

    @Test
    @Order(1)
    @DisplayName("Given Category object when create then returns Category object")
    void integrationTest_givenCategoryObject_whenCreate_ThenReturnsCategoryObject() throws IOException {

        var content = given()
                .spec(specification)
                .contentType("application/json")
                .body(categoryDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var createdCategory = objectMapper.readValue(content, Category.class);

        BeanUtils.copyProperties(createdCategory, categoryDTO);
        categoryId = createdCategory.getId();
        

        assertNotNull(createdCategory);
        assertFalse(createdCategory.getId().isEmpty());
        assertEquals("Test", createdCategory.getTitle());
        assertEquals("Category Test", createdCategory.getDescription());
        assertEquals("123", createdCategory.getOwnerId());
    }

    @Test
    @Order(2)
    @DisplayName("Given Category object when getAll then returns Category list")
    void integrationTest_givenCategoryObject_whenGetAll_ThenReturnsCategoryObject() throws IOException {
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
        List<Category> categories = objectMapper.readValue(content, new TypeReference<List<Category>>(){});

        assertNotNull(content);
        assertFalse(content.isEmpty());
        assertEquals("Test", categories.get(0).getTitle());
        assertEquals("Category Test", categories.get(0).getDescription());
        assertEquals("123", categories.get(0).getOwnerId());
    }

    @Test
    @Order(3)
    @DisplayName("Given Category Object when findAll then Returns Category List")
    void integrationTest_givenCategoryObject_whenFindAll_thenReturnsCategoryList() throws IOException {

        var content = given().spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Category[] categoryArray = objectMapper.readValue(content, Category[].class);
        List<Category> categoryList = Arrays.asList(categoryArray);

        Category foundCategory = categoryList.get(0);

        assertNotNull(foundCategory);
        assertNotNull(foundCategory.getId());
        assertEquals("Test", foundCategory.getTitle());
    }

    @Test
    @Order(4)
    @DisplayName("Given Category Object when Update then Returns Updated Category Object")
    void integrationTest_givenCategoryObject_whenUpdate_thenReturnsUpdatedCategoryObject() throws IOException {

        var content = given()
                .spec(specification)
                .contentType("application/json")
                .body(categoryDTO)
                .when()
                .put("/" + categoryId)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Category updatedCategory = objectMapper.readValue(content, Category.class);

        BeanUtils.copyProperties(updatedCategory, categoryDTO);
        categoryId = updatedCategory.getId();

        assertNotNull(updatedCategory);
        assertNotNull(updatedCategory.getId());
        assertEquals("Test", updatedCategory.getTitle());
    }

    @Test
    @Order(5)
    @DisplayName("Given Category object when delete then returns no content")
    void integrationTest_givenCategoryObject_whenDelete_thenReturnsNoContent() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .pathParam("id", categoryId)
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    @DisplayName("Given unexistent Category object when update then throws CategoryNotFoundException")
    void integrationTest_givenUnexistentCategoryObject_whenUpdate_thenThrowsCategoryNotFoundException() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .contentType("application/json")
                .body(categoryDTO)
                .when()
                .put("/" + categoryId)
                .then()
                .statusCode(404);
    }

    @Test
    @Order(7)
    @DisplayName("Given unexistent Category object when delete then throws CategoryNotFoundException")
    void integrationTest_givenUnexistentCategoryObject_whenDelete_thenThrowsCategoryNotFoundException() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .pathParam("id", categoryId)
                .when()
                .delete("{id}")
                .then()
                .statusCode(404);
    }

}