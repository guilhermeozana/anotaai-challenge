package com.gcampos.desafioanotaai.controller;

import com.gcampos.desafioanotaai.domain.dto.CategoryDTO;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryDTO categoryDTO){
        Category category = categoryService.create(categoryDTO);

        return ResponseEntity.ok(category);
    }
}
