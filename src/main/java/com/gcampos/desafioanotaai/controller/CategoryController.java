package com.gcampos.desafioanotaai.controller;

import com.gcampos.desafioanotaai.domain.dto.CategoryDTO;
import com.gcampos.desafioanotaai.domain.model.Category;
import com.gcampos.desafioanotaai.service.CategoryService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<Category> update(@PathParam("id") String id, @RequestBody CategoryDTO categoryDTO){
        Category category = categoryService.update(id, categoryDTO);

        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> delete(@PathParam("id") String id){
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }


}
