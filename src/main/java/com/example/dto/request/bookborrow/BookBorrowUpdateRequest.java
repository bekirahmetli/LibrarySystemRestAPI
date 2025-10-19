package com.example.dto.request.bookborrow;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookBorrowUpdateRequest {
    @Positive(message = "ID pozitif olmalı")
    private int id;
    @NotNull(message = "Return date null olamaz")
    private LocalDate returnDate;
}
