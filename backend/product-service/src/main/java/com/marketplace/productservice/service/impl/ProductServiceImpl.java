package com.marketplace.productservice.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.JsonData;
import com.marketplace.productservice.dto.ProductResponse;
import com.marketplace.productservice.dto.ProductSearchRequest;
import com.marketplace.productservice.dto.ProductSummaryResponse;
import com.marketplace.productservice.mapper.ProductMapper;
import com.marketplace.productservice.model.ProductElastic;
import com.marketplace.productservice.repository.mongo.ProductRepository;
import com.marketplace.productservice.repository.elastic.ProductSearchRepository;
import com.marketplace.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ElasticsearchClient elasticClient;
    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;

    @Override
    public Page<ProductSummaryResponse> searchApprovedSummary(ProductSearchRequest request) {

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        int from = page * size;

        List<Query> mustQueries = new ArrayList<>();
        List<Query> filterQueries = new ArrayList<>();

        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            mustQueries.add(QueryBuilders.multiMatch(m -> m
                    .query(request.getQuery())
                    .fields("name", "description")
            ));
        }


        if (request.getCategoryId() != null && !request.getCategoryId().isBlank()) {
            filterQueries.add(QueryBuilders.term(t -> t
                    .field("categoryId")
                    .value(request.getCategoryId())
            ));
        }


        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            filterQueries.add(QueryBuilders.range(r -> {
                if (request.getMinPrice() != null) r.gte(JsonData.of(request.getMinPrice().doubleValue()));
                if (request.getMaxPrice() != null) r.lte(JsonData.of(request.getMaxPrice()));
                return r.field("price");
            }));
        }


        if (request.getMinRating() != null || request.getMaxRating() != null) {
            filterQueries.add(QueryBuilders.range(r -> {
                if (request.getMinRating() != null) r.gte(JsonData.of(request.getMinRating()));
                if (request.getMaxRating() != null) r.lte(JsonData.of(request.getMaxRating()));
                return r.field("averageRating");
            }));
        }


        Query finalQuery = QueryBuilders.bool(b -> b
                .must(mustQueries)
                .filter(filterQueries)
        );


        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("products")
                .from(from)
                .size(size)
                .query(finalQuery)
        );


        SearchResponse<ProductElastic> response;
        try {
            response = elasticClient.search(searchRequest, ProductElastic.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка поиска в Elasticsearch", e);
        }


        List<ProductSummaryResponse> content = response.hits().hits().stream()
                .map(hit -> ProductMapper.toProductSummaryResponse(hit.source()))
                .toList();

        return new PageImpl<>(content, PageRequest.of(page, size), response.hits().total().value());
    }

    @Override
    public ProductResponse getProduct(Long id) {

        return new ProductResponse()
    }


}