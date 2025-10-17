package com.example.business.abstracts;

import com.example.entities.Author;
import org.springframework.data.domain.Page;

public interface IAuthorService {
    Author save(Author author);
    Author get(int id);
    Author update(Author author);
    boolean delete(int id);
    Page<Author> cursor(int page,int pageSize);
}
