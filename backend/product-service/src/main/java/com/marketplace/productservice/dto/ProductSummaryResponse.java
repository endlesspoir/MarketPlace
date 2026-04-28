package com.marketplace.productservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ProductSummaryResponse {

    private String id;

    private String name;

    private String description;

    private BigDecimal price;

    private List<String> imagesUrl;

    private Double averageRating;

}