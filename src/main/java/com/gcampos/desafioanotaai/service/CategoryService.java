package com.gcampos.desafioanotaai.service;

import com.gcampos.desafioanotaai.domain.dto.CategoryDTO;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.domain.exception.CategoryNotFoundException;
import com.gcampos.desafioanotaai.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    public Category create(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO);

        return categoryRepository.save(category);
    }

    public Category update(String id, CategoryDTO categoryDTO) {
        Category category = findById(id).orElseThrow(CategoryNotFoundException::new);

        BeanUtils.copyProperties(categoryDTO, category);

        return categoryRepository.save(category);
    }

    public void delete(String id) {
        Category category = findById(id).orElseThrow(CategoryNotFoundException::new);

        categoryRepository.delete(category);
    }
}
