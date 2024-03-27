package com.gcampos.desafioanotaai.service;

import com.gcampos.desafioanotaai.domain.dto.MessageDTO;
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

    private final AwsSnsService snsService;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(String id){
        return productRepository.findById(id);
    }

    public Product create(ProductDTO productDTO) {
        categoryService.findById(productDTO.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        Product product = new Product(productDTO);

        Product savedProduct = productRepository.save(product);

        snsService.publish(new MessageDTO(savedProduct.toString()));

        return savedProduct;
    }

    public Product update(String id, ProductDTO productDTO) {
        Product product = findById(id).orElseThrow(ProductNotFoundException::new);

        if(!productDTO.categoryId().isEmpty()) {
            categoryService.findById(productDTO.categoryId())
                    .orElseThrow(CategoryNotFoundException::new);

            product.setCategoryId(productDTO.categoryId());
        }

        if(!productDTO.title().isEmpty()) product.setTitle(productDTO.title());
        if(!productDTO.description().isEmpty()) product.setDescription(productDTO.description());
        if(!(productDTO.price() == null)) product.setPrice(productDTO.price());

        Product updatedProduct = productRepository.save(product);

        snsService.publish(new MessageDTO(updatedProduct.toString()));

        return updatedProduct;
    }

    public void delete(String id) {
        Product product = findById(id).orElseThrow(ProductNotFoundException::new);

        productRepository.delete(product);
    }
}
