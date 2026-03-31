package com.marketplace.productservice.mapper;

import com.marketplace.productservice.dto.ProductSummaryResponse;
import com.marketplace.productservice.model.Product;
import com.marketplace.productservice.model.ProductElastic;


public class ProductMapper {

    public static ProductElastic toElastic(Product p) {
        return new ProductElastic()
                .setId(p.getId())
                .setName(p.getName())
                .setDescription(p.getDescription())
                .setPrice(p.getPrice())
                .setAverageRating(p.getAverageRating())
                .setImagesUrl(p.getImagesUrl())
                .setCategoryId(p.getCategoryId());
    }

    public static ProductSummaryResponse toProductSummaryResponse(ProductElastic e) {
        return new ProductSummaryResponse()
                .setAverageRating(e.getAverageRating())
                .setId(e.getId())
                .setName(e.getName())
                .setPrice(e.getPrice())
                .setImagesUrl(e.getImagesUrl())
                .setDescription(e.getDescription());
    }
}