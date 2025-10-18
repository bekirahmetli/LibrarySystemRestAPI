package com.example.business.abstracts;

import com.example.entities.Category;
import org.springframework.data.domain.Page;

public interface ICategoryService {
    Category save(Category category);
    Category get(int id);
    Category update(Category category);
    String delete(int id);
    Page<Category> cursor(int page, int pageSize);
}
