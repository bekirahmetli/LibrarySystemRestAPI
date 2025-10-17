package com.example.dto.request.publisher;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherUpdateRequest {
    @NotNull
    @Positive(message = "ID pozitif olmalı")
    private int id;
    @NotNull(message = "Publisher name null olamaz")
    private String publisherName;
    private int establishmentYear;
    @NotNull(message = "Publisher adress null olamaz")
    private String address;
}
