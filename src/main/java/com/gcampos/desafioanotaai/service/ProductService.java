package com.gcampos.desafioanotaai.service;

import com.gcampos.desafioanotaai.domain.dto.ProductDTO;
import com.gcampos.desafioanotaai.domain.exception.CategoryNotFoundException;
import com.gcampos.desafioanotaai.domain.exception.ProductNotFoundException;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.domain.model.Product;
import com.gcampos.desafioanotaai.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final CategoryService categoryService;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(String id){
        return productRepository.findById(id);
    }

    public Product create(ProductDTO productDTO) {
        Category category = categoryService.findById(productDTO.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        Product product = new Product(productDTO, category);

        return productRepository.save(product);
    }

    public Product update(String id, ProductDTO productDTO) {
        Product product = findById(id).orElseThrow(ProductNotFoundException::new);

        BeanUtils.copyProperties(productDTO, product);

        categoryService.findById(productDTO.categoryId()).ifPresent(product::setCategory);

        return productRepository.save(product);
    }

    public void delete(String id) {
        Product product = findById(id).orElseThrow(ProductNotFoundException::new);

        productRepository.delete(product);
    }
}
