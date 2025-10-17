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
    @Positive
    private int id;
    @NotNull
    private String  authorName;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private String country;
}
