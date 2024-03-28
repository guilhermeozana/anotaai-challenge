package com.gcampos.desafioanotaai.util;

import com.gcampos.desafioanotaai.domain.dto.CategoryDTO;
import com.gcampos.desafioanotaai.domain.model.Category;

public class CategoryCreator {

    public static Category buildCategory() {
        return Category
                .builder()
                .id("1")
                .title("Test")
                .description("Category Test")
                .ownerId("123")
                .build();
    }

    public static CategoryDTO buildCategoryDTO() {
        return CategoryDTO
                .builder()
                .title("Test")
                .description("Category Test")
                .ownerId("123")
                .build();
    }
}
