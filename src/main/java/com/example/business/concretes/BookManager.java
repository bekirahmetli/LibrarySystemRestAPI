package com.example.business.concretes;

import com.example.business.abstracts.IBookService;
import com.example.core.exception.NotFoundException;
import com.example.core.utils.Message;
import com.example.dao.AuthorRepo;
import com.example.dao.BookRepo;
import com.example.dao.CategoryRepo;
import com.example.dao.PublisherRepo;
import com.example.dto.request.book.BookSaveRequest;
import com.example.dto.request.book.BookUpdateRequest;
import com.example.entities.Author;
import com.example.entities.Book;
import com.example.entities.Category;
import com.example.entities.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BookManager implements IBookService {
    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final PublisherRepo publisherRepo;
    private final CategoryRepo categoryRepo;

    public BookManager(BookRepo bookRepo, AuthorRepo authorRepo, PublisherRepo publisherRepo, CategoryRepo categoryRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.publisherRepo = publisherRepo;
        this.categoryRepo = categoryRepo;
    }


    @Override
    public Book save(BookSaveRequest request) {

        Author author = authorRepo.findById(request.getAuthorId())
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));

        Publisher publisher = publisherRepo.findById(request.getPublisherId())
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));

        List<Category> categories = Collections.emptyList();
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            categories = categoryRepo.findAllById(request.getCategoryIds());

            if (categories.size() != request.getCategoryIds().size()) {
                throw new NotFoundException(Message.NOT_FOUND);
            }
        }

        Book book = new Book();
        book.setBookName(request.getBookName());
        book.setPublicationYear(request.getPublicationYear());
        book.setStock(request.getStock());
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setCategories(categories);

        for (Category c : categories) {
            if (c.getBooks() == null) {
                c.setBooks(new ArrayList<>());
            }
            if (!c.getBooks().contains(book)) {
                c.getBooks().add(book);
            }
        }

        return bookRepo.save(book);
    }

    @Override
    public Book get(int id) {
        return this.bookRepo.findById(id).orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));
    }

    @Override
    public Book update(BookUpdateRequest request) {

        Book existing = bookRepo.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));

        Author author = authorRepo.findById(request.getAuthorId())
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));

        Publisher publisher = publisherRepo.findById(request.getPublisherId())
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));

        List<Category> categories = Collections.emptyList();
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            categories = categoryRepo.findAllById(request.getCategoryIds());

            if (categories.size() != request.getCategoryIds().size()) {
                throw new NotFoundException(Message.NOT_FOUND);
            }
        }

        existing.setBookName(request.getBookName());
        existing.setPublicationYear(request.getPublicationYear());
        existing.setStock(request.getStock());
        existing.setAuthor(author);
        existing.setPublisher(publisher);
        existing.setCategories(categories);

        List<Category> oldCategories = existing.getCategories();
        if (oldCategories != null) {
            for (Category oc : oldCategories) {
                if (oc.getBooks() != null) {
                    oc.getBooks().remove(existing);
                }
            }
        }

        existing.setCategories(categories);
        for (Category c : categories) {
            if (c.getBooks() == null) {
                c.setBooks(new ArrayList<>());
            }
            if (!c.getBooks().contains(existing)) {
                c.getBooks().add(existing);
            }
        }

        return bookRepo.save(existing);
    }


    @Override
    public boolean delete(int id) {
        Book book = this.get(id);
        this.bookRepo.delete(book);
        return true;
    }

    @Override
    public Page<Book> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.bookRepo.findAll(pageable);
    }
}
