package com.marketplace.productservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSearchRequest {

    private String query;
    private String categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Double minRating;
    private Double maxRating;
    private Integer page;
    private Integer size;
}