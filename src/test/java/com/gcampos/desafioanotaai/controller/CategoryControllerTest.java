package com.gcampos.desafioanotaai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcampos.desafioanotaai.domain.dto.CategoryDTO;
import com.gcampos.desafioanotaai.domain.dto.MessageDTO;
import com.gcampos.desafioanotaai.domain.exception.CategoryNotFoundException;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.service.CategoryService;
import com.gcampos.desafioanotaai.util.CategoryCreator;
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
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    public void setup() {
        //Given
        category = CategoryCreator.buildCategory();

        categoryDTO = CategoryCreator.buildCategoryDTO();
    }

    @Test
    @DisplayName("Given Category List when getAll then Return Category List")
    void testGivenCategoryList_whenGetAll_thenReturnCategoryList() throws Exception {
        // Given
        List<Category> categoryList = List.of(category);

        given(categoryService.getAll()).willReturn(categoryList);

        // When
        ResultActions response = mockMvc.perform(get("/api/categories"));

        // Then
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(categoryList.size())));
    }

    @Test
    @DisplayName("Given Category Object when create then Return Saved Category")
    void testGivenCategoryObject_whenCreate_thenReturnSavedCategory() throws Exception {
        // Given
        given(categoryService.create(any(CategoryDTO.class)))
                .willReturn(category);

        // When
        ResultActions response = mockMvc.perform(post("/api/categories")
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
    @DisplayName("Given Updated Category when Update then Return Updated Category Object")
    void testGivenUpdatedCategory_WhenUpdate_thenReturnUpdatedCategoryObject() throws JsonProcessingException, Exception {

        // Given
        String categoryId = "1";
        given(categoryService.findById(categoryId)).willReturn(category);
        given(categoryService.update(anyString(), any(CategoryDTO.class)))
                .willReturn(category);

        // When
        Category updatedCategory = CategoryCreator.buildCategory();

        ResultActions response = mockMvc.perform(put("/api/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategory)));

        // Then
        response.
                andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(updatedCategory.getId())))
                .andExpect(jsonPath("$.title", is(updatedCategory.getTitle())))
                .andExpect(jsonPath("$.description", is(updatedCategory.getDescription())));
    }

    @Test
    @DisplayName("Given Nonexistent Category when Update then Returns CategoryNotFoundException")
    void testGivenNonexistentCategory_WhenUpdate_thenCategoryNotFoundException() throws JsonProcessingException, Exception {

        // Given
        String categoryId = "1";
        doThrow(CategoryNotFoundException.class).when(categoryService).update(anyString(), any(CategoryDTO.class));

        // When

        ResultActions response = mockMvc.perform(put("/api/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)));

        // Then
        response.
                andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Given categoryId when Delete then Removes Category")
    void testGivenCategoryId_WhenDelete_thenReturnNotContent() throws Exception {

        // Given
        String categoryId = "1";
        given(categoryService.findById(categoryId)).willReturn(category);
        willDoNothing().given(categoryService).delete(anyString());

        // When
        ResultActions response = mockMvc.perform(delete("/api/categories/{id}", categoryId));

        // Then
        response.
                andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("Given Nonexistent Category when delete then throws CategoryNotFoundException")
    void testGivenNonExistentCategory_WhenDelete_thenThrowsCategoryNotFoundException() throws JsonProcessingException, Exception {

        // Given
        String categoryId = "1";
        doThrow(CategoryNotFoundException.class).when(categoryService).delete(anyString());

        // When
        ResultActions response = mockMvc.perform(delete("/api/categories/{id}", categoryId));

        // Then
        response.
                andExpect(status().isNotFound())
                .andDo(print());
    }

}
