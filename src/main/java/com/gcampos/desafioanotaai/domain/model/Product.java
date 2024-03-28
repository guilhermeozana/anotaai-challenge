package com.gcampos.desafioanotaai.domain.model;

import com.gcampos.desafioanotaai.domain.dto.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
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


    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", id);
        jsonObject.put("title", title);
        jsonObject.put("description", description);
        jsonObject.put("ownerId", ownerId);
        jsonObject.put("price", price);
        jsonObject.put("categoryId", categoryId);
        jsonObject.put("type", "product");

        return jsonObject.toString();
    }
}
