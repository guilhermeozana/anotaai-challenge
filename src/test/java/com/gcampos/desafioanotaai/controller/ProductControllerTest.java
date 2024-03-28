package com.gcampos.desafioanotaai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcampos.desafioanotaai.domain.dto.ProductDTO;
import com.gcampos.desafioanotaai.domain.exception.ProductNotFoundException;
import com.gcampos.desafioanotaai.domain.model.Product;
import com.gcampos.desafioanotaai.service.ProductService;
import com.gcampos.desafioanotaai.util.ProductCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService categoryService;

    private Product category;
    private ProductDTO categoryDTO;

    @BeforeEach
    public void setup() {
        //Given
        category = ProductCreator.buildProduct();

        categoryDTO = ProductCreator.buildProductDTO();
    }

    @Test
    @DisplayName("Given Product List when getAll then Return Product List")
    void testGivenProductList_whenGetAll_thenReturnProductList() throws Exception {
        // Given
        List<Product> categoryList = List.of(category);

        given(categoryService.getAll()).willReturn(categoryList);

        // When
        ResultActions response = mockMvc.perform(get("/api/products"));

        // Then
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(categoryList.size())));
    }

    @Test
    @DisplayName("Given Product Object when create then Return Saved Product")
    void testGivenProductObject_whenCreate_thenReturnSavedProduct() throws Exception {
        // Given
        given(categoryService.create(any(ProductDTO.class)))
                .willReturn(category);

        // When
        ResultActions response = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)));

        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(category.getId())))
                .andExpect(jsonPath("$.title", is(category.getTitle())))
                .andExpect(jsonPath("$.description", is(category.getDescription())));
    }

    @Test
    @DisplayName("Given Updated Product when Update then Return Updated Product Object")
    void testGivenUpdatedProduct_WhenUpdate_thenReturnUpdatedProductObject() throws JsonProcessingException, Exception {

        // Given
        String categoryId = "1";
        given(categoryService.update(anyString(), any(ProductDTO.class)))
                .willReturn(category);

        // When
        Product updatedProduct = ProductCreator.buildProduct();

        ResultActions response = mockMvc.perform(put("/api/products/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)));

        // Then
        response.
                andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(updatedProduct.getId())))
                .andExpect(jsonPath("$.title", is(updatedProduct.getTitle())))
                .andExpect(jsonPath("$.description", is(updatedProduct.getDescription())));
    }

    @Test
    @DisplayName("Given Nonexistent Product when Update then Returns ProductNotFoundException")
    void testGivenNonexistentProduct_WhenUpdate_thenProductNotFoundException() throws JsonProcessingException, Exception {

        // Given
        String categoryId = "1";
        doThrow(ProductNotFoundException.class).when(categoryService).update(anyString(), any(ProductDTO.class));

        // When

        ResultActions response = mockMvc.perform(put("/api/products/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)));

        // Then
        response.
                andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Given categoryId when Delete then Removes Product")
    void testGivenProductId_WhenDelete_thenReturnNotContent() throws Exception {

        // Given
        String categoryId = "1";
        willDoNothing().given(categoryService).delete(anyString());

        // When
        ResultActions response = mockMvc.perform(delete("/api/products/{id}", categoryId));

        // Then
        response.
                andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("Given Nonexistent Product when delete then throws ProductNotFoundException")
    void testGivenNonExistentProduct_WhenDelete_thenThrowsProductNotFoundException() throws JsonProcessingException, Exception {

        // Given
        String categoryId = "1";
        doThrow(ProductNotFoundException.class).when(categoryService).delete(anyString());

        // When
        ResultActions response = mockMvc.perform(delete("/api/products/{id}", categoryId));

        // Then
        response.
                andExpect(status().isNotFound())
                .andDo(print());
    }
}
