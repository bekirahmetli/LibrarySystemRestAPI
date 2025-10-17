package com.example.dto.request.publisher;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherSaveRequest {
    @NotNull(message = "Publisher name null olamaz")
    private String publisherName;
    private int establishmentYear;
    @NotNull(message = "Publisher adress null olamaz")
    private String address;
}
