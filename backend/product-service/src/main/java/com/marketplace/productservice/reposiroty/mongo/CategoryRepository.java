package com.marketplace.productservice.reposiroty.mongo;

import com.marketplace.productservice.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CategoryRepository extends MongoRepository<Category, String> {

}
