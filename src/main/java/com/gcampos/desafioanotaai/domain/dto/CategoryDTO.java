package com.gcampos.desafioanotaai.domain.dto;

import lombok.Builder;

@Builder
public record CategoryDTO(
        String title,
        String description,
        String ownerId
) {}
