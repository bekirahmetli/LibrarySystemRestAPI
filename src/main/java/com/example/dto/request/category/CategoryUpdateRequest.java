package com.example.dto.request.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {
    @NotNull
    @Positive(message = "ID pozitif olmalı")
    private int id;
    @NotNull(message = "Category name null olamaz")
    private String categoryName;
    private String description;
}
