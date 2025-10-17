package com.example.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorSaveRequest {
    @NotNull
    private String  authorName;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private String country;
}
