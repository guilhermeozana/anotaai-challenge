package com.gcampos.desafioanotaai.domain.dto;


import lombok.Builder;

@Builder
public record ProductDTO(
        String title,

        String description,

        String ownerId,

        Double price,

        String categoryId
) {}
