package com.marketplace.productservice.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "products")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank
    @Indexed(unique = true)
    private String slug;

    private String description;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;


    @NotBlank
    @Indexed
    private String categoryId;

    @NotNull
    @Indexed
    private Long sellerId;

    private List<String> imagesUrl;

    private Map<String, String> attributes;

    @NotNull
    @Min(0)
    private Integer quantity;

    @DecimalMin("0.0")
    private Double averageRating = 0.0;

    @Min(0)
    private Integer reviewCount = 0;

    @NotNull
    private ProductStatus status = ProductStatus.PENDING;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}