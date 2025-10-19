package com.example.business.concretes;

import com.example.business.abstracts.IBookBorrowService;
import com.example.core.exception.NotFoundException;
import com.example.core.utils.Message;
import com.example.dao.BookBorrowRepo;
import com.example.dao.BookRepo;
import com.example.dto.request.bookborrow.BookBorrowSaveRequest;
import com.example.dto.request.bookborrow.BookBorrowUpdateRequest;
import com.example.entities.Book;
import com.example.entities.BookBorrowing;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
public class BookBorrowManager implements IBookBorrowService {
    private final BookBorrowRepo bookBorrowRepo;
    private final BookRepo bookRepo;

    public BookBorrowManager(BookBorrowRepo bookBorrowRepo, BookRepo bookRepo) {
        this.bookBorrowRepo = bookBorrowRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public BookBorrowing save(BookBorrowSaveRequest request) {

        Book book = bookRepo.findById(request.getBookId())
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));

        if (book.getStock() <= 0) {
            throw new IllegalStateException(Message.NO_BOOK_IN_STOCK_MESSAGE);
        }

        book.setStock(book.getStock() - 1);

        BookBorrowing bb = new BookBorrowing();
        bb.setBorrowerName(request.getBorrowerName());
        bb.setBorrowerEmail(request.getBorrowerEmail());
        bb.setBorrowingDate(request.getBorrowingDate() != null ? request.getBorrowingDate() : LocalDate.now());
        bb.setReturnDate(null);
        bb.setBook(book);

        return bookBorrowRepo.save(bb);
    }

    @Override
    public BookBorrowing get(int id) {
        return this.bookBorrowRepo.findById(id).orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));
    }

    @Override
    public BookBorrowing update(BookBorrowUpdateRequest request) {

        BookBorrowing bb = bookBorrowRepo.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));

        if (bb.getReturnDate() != null) {
            throw new IllegalStateException(Message.RECORD_ALREADY_RETURNED_MESSAGE);
        }

        bb.setReturnDate(request.getReturnDate());

        Book book = bb.getBook();
        book.setStock(book.getStock() + 1);

        return bookBorrowRepo.save(bb);
    }

    @Override
    public boolean delete(int id) {
        BookBorrowing bookBorrowing = this.get(id);
        this.bookBorrowRepo.delete(bookBorrowing);
        return true;
    }

    @Override
    public Page<BookBorrowing> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.bookBorrowRepo.findAll(pageable);
    }
}
