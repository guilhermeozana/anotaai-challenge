package com.gcampos.desafioanotaai.repository;

import com.gcampos.desafioanotaai.domain.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
}
