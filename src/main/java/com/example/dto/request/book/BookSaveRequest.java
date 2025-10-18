package com.example.dto.request.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSaveRequest {
    @NotNull(message = "Book name null olamaz")
    private String bookName;
    private int publicationYear;
    @Min(0)
    private int stock;
    @Positive(message = "Author ID pozitif olmalı")
    @NotNull
    private int authorId;
    @Positive(message = "Publisher ID pozitif olmalı")
    @NotNull
    private int publisherId;
    private List<Integer> categoryIds;
}
