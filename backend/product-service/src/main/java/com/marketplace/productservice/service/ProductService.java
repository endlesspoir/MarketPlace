package com.marketplace.productservice.service;

import com.marketplace.productservice.dto.ProductResponse;
import com.marketplace.productservice.dto.ProductSearchRequest;
import com.marketplace.productservice.dto.ProductSummaryResponse;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<ProductSummaryResponse> searchApprovedSummary(ProductSearchRequest request);

    ProductResponse getProduct(Long id);
}
