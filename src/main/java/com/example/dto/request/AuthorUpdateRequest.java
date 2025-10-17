package com.example.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorUpdateRequest {
    @NotNull
    @Positive(message = "ID pozitif olmalı")
    private int id;
    @NotNull(message = "Author name null olamaz")
    private String  authorName;
    @NotNull(message = "Author birth date null olamaz")
    private LocalDate birthDate;
    @NotNull(message = "Author country null olamaz")
    private String country;
}
