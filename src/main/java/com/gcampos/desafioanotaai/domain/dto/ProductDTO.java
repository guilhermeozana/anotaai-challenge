package com.gcampos.desafioanotaai.domain.dto;

import com.gcampos.desafioanotaai.domain.model.Category;

public record ProductDTO(
        String title,

        String description,

        String ownerId,

        Double price,

        String categoryId
) {}
