package com.example.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorSaveRequest {
    @NotNull(message = "Author name null olamaz")
    private String  authorName;
    @NotNull(message = "Author birth date null olamaz")
    private LocalDate birthDate;
    @NotNull(message = "Author country null olamaz")
    private String country;
}
