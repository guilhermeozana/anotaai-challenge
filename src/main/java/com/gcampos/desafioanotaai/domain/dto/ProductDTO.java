package com.gcampos.desafioanotaai.domain.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Builder
public record ProductDTO(
        String title,

        String description,

        String ownerId,

        Double price,

        String categoryId
) {}
