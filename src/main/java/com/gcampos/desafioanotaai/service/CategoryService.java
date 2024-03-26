package com.gcampos.desafioanotaai.service;

import com.gcampos.desafioanotaai.domain.dto.CategoryDTO;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category create(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO);

        return categoryRepository.save(category);
    }
}
