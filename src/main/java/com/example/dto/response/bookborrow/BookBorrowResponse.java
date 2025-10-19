package com.example.dto.response.bookborrow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookBorrowResponse {
    private int id;
    private String borrowerName;
    private String borrowerEmail;
    private LocalDate borrowingDate;
    private LocalDate returnDate;
    private int bookId;
}
