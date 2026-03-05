package com.marketplace.productservice.reposiroty.mongo;

import com.marketplace.productservice.model.Product;
import com.marketplace.productservice.model.ProductStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface ProductRepository extends MongoRepository<Product, String> {

        List<Product> findAllByProductStatus(ProductStatus productStatus);
}
