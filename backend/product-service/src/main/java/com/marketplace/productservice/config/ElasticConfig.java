package com.marketplace.productservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
        basePackages = "com.marketplace.productservice.repository.elastic"
)
public class ElasticConfig {
}