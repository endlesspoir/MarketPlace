package com.marketplace.productservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductSummaryResponse {

    private String id;

    private String name;

    private String slug;

    private BigDecimal price;

    private String currency;

    private List<String> imagesUrl;

    private Double averageRating;

}