package com.marketplace.productservice.repository.elastic;

import com.marketplace.productservice.model.ProductElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductElastic, String> {
}