package com.gcampos.desafioanotaai.sevice;

import com.gcampos.desafioanotaai.domain.dto.MessageDTO;
import com.gcampos.desafioanotaai.domain.dto.CategoryDTO;
import com.gcampos.desafioanotaai.domain.exception.CategoryNotFoundException;
import com.gcampos.desafioanotaai.domain.exception.CategoryNotFoundException;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.repository.CategoryRepository;
import com.gcampos.desafioanotaai.service.AwsSnsService;
import com.gcampos.desafioanotaai.service.CategoryService;
import com.gcampos.desafioanotaai.util.CategoryCreator;
import com.gcampos.desafioanotaai.util.CategoryCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AwsSnsService awsSnsService;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    public void setup() {
        //Given
        category = CategoryCreator.buildCategory();
        categoryDTO = CategoryCreator.buildCategoryDTO();
    }

    @DisplayName("Test Given Category List when findAll then Returns Category List")
    @Test
    void testGivenCategoryList_whenFindAll_thenReturnsCategoryList() {

        // Given
        given(categoryRepository.findAll()).willReturn(List.of(CategoryCreator.buildCategory()));

        // When
        List<Category> categoryList = categoryService.getAll();

        // Then
        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
        assertEquals("Test", categoryList.get(0).getTitle());
        assertEquals("1", categoryList.get(0).getId());
    }

    @DisplayName("Test Given Id when findById then Returns Category Object")
    @Test
    void testGivenId_whenFindById_thenReturnsCategoryObject() {

        // Given
        given(categoryRepository.findById(anyString())).willReturn(Optional.of(category));

        // When
        Category categoryById = categoryService.findById("1");

        // Then
        assertNotNull(categoryById);
        assertEquals("Test", categoryById.getTitle());
        assertEquals("1", categoryById.getId());
    }

    @DisplayName("Test Given Id when findById and Category is not found then Throws CategoryNotFoundException")
    @Test
    void testGivenId_whenFindByIdAndCategoryIsNotFound_thenThrowsCategoryNotFoundException() {

        // Given
        given(categoryRepository.findById(anyString())).willThrow(CategoryNotFoundException.class);

        // Then
        assertThrows(CategoryNotFoundException.class, () -> {
            // When
            categoryService.findById("1");
        });
    }

    @DisplayName("Test Given Category when Create then Returns Category Object")
    @Test
    void testGivenCategory_whenCreate_thenReturnsCategoryObject() {

        // Given
        given(categoryRepository.save(any(Category.class))).willReturn(category);
        willDoNothing().given(awsSnsService).publish(any(MessageDTO.class));

        // When
        Category savedCategory = categoryService.create(categoryDTO);

        // Then
        assertNotNull(savedCategory);
        assertEquals("Test", savedCategory.getTitle());
        assertEquals("1", savedCategory.getId());
    }

    @DisplayName("Test Given Category when Update then Returns Category Object")
    @Test
    void testGivenCategory_WhenUpdate_thenReturnsCategoryObject() {

        // Given
        given(categoryRepository.findById(anyString())).willReturn(Optional.of(category));
        given(categoryRepository.save(any(Category.class))).willReturn(category);
        willDoNothing().given(awsSnsService).publish(any(MessageDTO.class));

        // When
        Category updatedCategory = categoryService.update("1", categoryDTO);

        // Then
        assertNotNull(updatedCategory);
        assertEquals("Test", updatedCategory.getTitle());
        assertEquals("1", updatedCategory.getId());
    }

    @DisplayName("Test Given Category when Update and Category is not Found then Throws CategoryNotFoundException")
    @Test
    void testGivenCategory_WhenUpdateCategoryIsNotFound_thenThrowsCategoryNotFoundException() {

        // Given
        given(categoryRepository.findById(anyString())).willThrow(CategoryNotFoundException.class);

        // Then
        assertThrows(CategoryNotFoundException.class, () -> {
            // When
            categoryService.update("1", categoryDTO);
        });
    }

    @DisplayName("Test Given Id when Delete then Removes Category Object")
    @Test
    void testGivenId_WhenDelete_thenRemovesCategoryObject() {

        // Given
        given(categoryRepository.findById(anyString())).willReturn(Optional.of(category));
        willDoNothing().given(categoryRepository).delete(any(Category.class));
        willDoNothing().given(awsSnsService).publish(any(MessageDTO.class));

        // When
        categoryService.delete("1");

        // Then
        verify(categoryRepository, times(1)).delete(category);
    }

    @DisplayName("Test Given Category when Delete and Category is not Found then Throws CategoryNotFoundException")
    @Test
    void testGivenCategory_WhenDeleteAndCategoryIsNotFound_thenThrowsCategoryNotFoundException() {

        // Given
        given(categoryRepository.findById(anyString())).willThrow(CategoryNotFoundException.class);

        // Then
        assertThrows(CategoryNotFoundException.class, () -> {
            // When
            categoryService.delete("1");
        });
    }
}
