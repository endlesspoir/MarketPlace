package com.marketplace.productservice.service.impl;

import com.marketplace.productservice.dto.ProductSearchRequest;
import com.marketplace.productservice.dto.ProductSummaryResponse;
import com.marketplace.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Override
    public Page<ProductSummaryResponse> searchApprovedSummary(ProductSearchRequest request) {
        return null;
    }
}
