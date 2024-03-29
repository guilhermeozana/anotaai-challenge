package com.gcampos.desafioanotaai.sevice;

import com.gcampos.desafioanotaai.domain.dto.MessageDTO;
import com.gcampos.desafioanotaai.domain.dto.ProductDTO;
import com.gcampos.desafioanotaai.domain.exception.CategoryNotFoundException;
import com.gcampos.desafioanotaai.domain.exception.ProductNotFoundException;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.domain.model.Product;
import com.gcampos.desafioanotaai.repository.ProductRepository;
import com.gcampos.desafioanotaai.service.AwsSnsService;
import com.gcampos.desafioanotaai.service.CategoryService;
import com.gcampos.desafioanotaai.service.ProductService;
import com.gcampos.desafioanotaai.util.CategoryCreator;
import com.gcampos.desafioanotaai.util.ProductCreator;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private AwsSnsService awsSnsService;

    private Product product;
    private ProductDTO productDTO;

    private Category category;

    @BeforeEach
    public void setup() {
        //Given
        product = ProductCreator.buildProduct();
        productDTO = ProductCreator.buildProductDTO();

        category = CategoryCreator.buildCategory();
    }

    @DisplayName("Test Given Product List when findAll then Returns Product List")
    @Test
    void testGivenProductList_whenFindAll_thenReturnsProductList() {

        // Given
        given(productRepository.findAll()).willReturn(List.of(ProductCreator.buildProduct()));

        // When
        List<Product> productList = productService.getAll();

        // Then
        assertNotNull(productList);
        assertFalse(productList.isEmpty());
        assertEquals("Test", productList.get(0).getTitle());
        assertEquals("1", productList.get(0).getId());
    }

    @DisplayName("Test Given Product when Create then Returns Product Object")
    @Test
    void testGivenProduct_whenCreate_thenReturnsProductObject() {

        // Given
        given(categoryService.findById(anyString())).willReturn(category);
        given(productRepository.save(any(Product.class))).willReturn(product);
        willDoNothing().given(awsSnsService).publish(any(MessageDTO.class));

        // When
        Product savedProduct = productService.create(productDTO);

        // Then
        assertNotNull(savedProduct);
        assertEquals("Test", savedProduct.getTitle());
        assertEquals("1", savedProduct.getId());
    }

    @DisplayName("Test Given Product when Create and Category is not found then Throws CategoryNotFoundException")
    @Test
    void testGivenProduct_whenCreateAndCategoryIsNotFound_thenThrowsCategoryNotFoundException() {

        // Given
        given(categoryService.findById(anyString())).willThrow(CategoryNotFoundException.class);

        // Then
        assertThrows(CategoryNotFoundException.class, () -> {
            // When
            productService.create(productDTO);
        });
    }

    @DisplayName("Test Given Product when Update then Returns Product Object")
    @Test
    void testGivenProduct_WhenUpdate_thenReturnsProductObject() {

        // Given
        given(productRepository.findById(anyString())).willReturn(Optional.of(product));
        given(categoryService.findById(anyString())).willReturn(category);
        given(productRepository.save(any(Product.class))).willReturn(product);
        willDoNothing().given(awsSnsService).publish(any(MessageDTO.class));

        // When
        Product updatedProduct = productService.update("1", productDTO);

        // Then
        assertNotNull(updatedProduct);
        assertEquals("Test", updatedProduct.getTitle());
        assertEquals("1", updatedProduct.getId());
    }

    @DisplayName("Test Given Product when Update and Product is not Found then Throws ProductNotFoundException")
    @Test
    void testGivenProduct_WhenUpdateProductIsNotFound_thenThrowsProductNotFoundException() {

        // Given
        given(productRepository.findById(anyString())).willThrow(ProductNotFoundException.class);

        // Then
        assertThrows(ProductNotFoundException.class, () -> {
            // When
            productService.update("1", productDTO);
        });
    }

    @DisplayName("Test Given Id when Delete then Removes Product Object")
    @Test
    void testGivenId_WhenDelete_thenRemovesProductObject() {

        // Given
        given(productRepository.findById(anyString())).willReturn(Optional.of(product));
        willDoNothing().given(productRepository).delete(any(Product.class));
        willDoNothing().given(awsSnsService).publish(any(MessageDTO.class));

        // When
        productService.delete("1");

        // Then
        verify(productRepository, times(1)).delete(product);
    }

    @DisplayName("Test Given Product when Delete and Product is not Found then Throws ProductNotFoundException")
    @Test
    void testGivenProduct_WhenDeleteProductIsNotFound_thenThrowsProductNotFoundException() {

        // Given
        given(productRepository.findById(anyString())).willThrow(ProductNotFoundException.class);

        // Then
        assertThrows(ProductNotFoundException.class, () -> {
            // When
            productService.delete("1");
        });
    }
}
