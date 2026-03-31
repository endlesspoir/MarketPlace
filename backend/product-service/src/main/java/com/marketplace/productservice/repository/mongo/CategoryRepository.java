package com.marketplace.productservice.repository.mongo;

import com.marketplace.productservice.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CategoryRepository extends MongoRepository<Category, String> {

}
