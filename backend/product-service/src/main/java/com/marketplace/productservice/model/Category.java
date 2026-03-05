package com.marketplace.productservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "categories")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Indexed(unique = true)
    private String slug;

    private String parentId;

    private String path;

    private Integer level;

    @CreatedDate
    private LocalDateTime createdAt;
}