package com.gcampos.desafioanotaai.controller;

import com.gcampos.desafioanotaai.domain.dto.ProductDTO;
import com.gcampos.desafioanotaai.domain.model.Product;
import com.gcampos.desafioanotaai.service.ProductService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAll(){
        List<Product> productList = productService.getAll();

        return ResponseEntity.ok(productList);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductDTO productDTO){
        Product product = productService.create(productDTO);

        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable("id") String id, @RequestBody ProductDTO productDTO){
        Product product = productService.update(id, productDTO);

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> delete(@PathVariable("id") String id){
        productService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
