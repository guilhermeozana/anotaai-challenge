package com.gcampos.desafioanotaai.controller;

import com.gcampos.desafioanotaai.domain.dto.CategoryDTO;
import com.gcampos.desafioanotaai.domain.exception.CategoryNotFoundException;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAll(){
        List<Category> categoryList = categoryService.getAll();

        return ResponseEntity.ok(categoryList);
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryDTO categoryDTO){
        Category category = categoryService.create(categoryDTO);

        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable("id") String id, @RequestBody CategoryDTO categoryDTO){
        try {
           Category category = categoryService.update(id, categoryDTO);

            return ResponseEntity.ok(category);

        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> delete(@PathVariable("id") String id){
        try {
            categoryService.delete(id);

            return ResponseEntity.noContent().build();

        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found", e);
        }
    }


}
