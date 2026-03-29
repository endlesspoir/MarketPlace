package com.marketplace.productservice.controller;

import com.marketplace.productservice.dto.ProductResponse;
import com.marketplace.productservice.dto.ProductSearchRequest;
import com.marketplace.productservice.dto.ProductSummaryResponse;
import com.marketplace.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class PublicProductController {

    private final ProductService productService;

    @GetMapping
    public Page<ProductSummaryResponse> getProducts(@Valid ProductSearchRequest productSearchRequest) {
        return productService.searchApprovedSummary(productSearchRequest);
    }
}
