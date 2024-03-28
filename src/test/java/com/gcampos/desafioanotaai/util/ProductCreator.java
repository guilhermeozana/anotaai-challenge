package com.gcampos.desafioanotaai.util;

import com.gcampos.desafioanotaai.domain.dto.ProductDTO;
import com.gcampos.desafioanotaai.domain.model.Product;

public class ProductCreator {

    public static Product buildProduct() {
        return Product
                .builder()
                .id("1")
                .price(2.5)
                .title("Test")
                .description("Product Test")
                .ownerId("123")
                .categoryId("2")
                .build();
    }

    public static ProductDTO buildProductDTO() {
        return ProductDTO
                .builder()
                .price(2.5)
                .title("Test")
                .description("Product Test")
                .ownerId("123")
                .categoryId("2")
                .build();
    }
}
