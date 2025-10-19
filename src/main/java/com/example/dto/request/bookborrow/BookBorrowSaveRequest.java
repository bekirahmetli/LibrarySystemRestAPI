package com.example.dto.request.bookborrow;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookBorrowSaveRequest {
    @NotNull(message = "Borrow name null olamaz")
    private String borrowerName;
    @Email
    @NotNull(message = "borrow email null olamaz")
    private String borrowerEmail;
    private LocalDate borrowingDate;
    @Positive(message = "ID pozitif olmalı")
    private int bookId;
}