package com.gcampos.desafioanotaai.service;

import com.gcampos.desafioanotaai.domain.dto.CategoryDTO;
import com.gcampos.desafioanotaai.domain.dto.MessageDTO;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.domain.exception.CategoryNotFoundException;
import com.gcampos.desafioanotaai.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final AwsSnsService snsService;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category findById(String id) {
        return categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    public Category create(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO);

        Category savedCategory = categoryRepository.save(category);

        snsService.publish(new MessageDTO(savedCategory.toString()));

        return savedCategory;
    }

    public Category update(String id, CategoryDTO categoryDTO) {
        Category category = findById(id);

        if(!categoryDTO.title().isEmpty()) category.setTitle(categoryDTO.title());
        if(!categoryDTO.description().isEmpty()) category.setDescription(categoryDTO.description());

        Category updatedCategory = categoryRepository.save(category);

        snsService.publish(new MessageDTO(updatedCategory.toString()));

        return updatedCategory;
    }

    public void delete(String id) {
        Category category = findById(id);

        categoryRepository.delete(category);

        JSONObject idJson = new JSONObject()
            .put("id", category.getId())
            .put("ownerId", category.getOwnerId());

        snsService.publish(new MessageDTO(idJson.toString()));
    }
}
