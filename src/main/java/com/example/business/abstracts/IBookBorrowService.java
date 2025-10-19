package com.example.business.abstracts;

import com.example.dto.request.bookborrow.BookBorrowSaveRequest;
import com.example.dto.request.bookborrow.BookBorrowUpdateRequest;
import com.example.entities.BookBorrowing;
import org.springframework.data.domain.Page;

public interface IBookBorrowService {
    BookBorrowing save(BookBorrowSaveRequest request);
    BookBorrowing get(int id);
    BookBorrowing update(BookBorrowUpdateRequest request);
    boolean delete(int id);
    Page<BookBorrowing> cursor(int page, int pageSize);
}