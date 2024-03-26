package com.gcampos.desafioanotaai.domain.model;

import com.gcampos.desafioanotaai.domain.dto.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    private String id;

    private String title;

    private String description;

    private String ownerId;

    private Double price;

    private String categoryId;

    public Product(ProductDTO productDTO) {
        BeanUtils.copyProperties(productDTO, this);
    }
}
