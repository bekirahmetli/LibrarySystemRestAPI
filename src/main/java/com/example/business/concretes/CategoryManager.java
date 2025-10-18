package com.example.business.concretes;

import com.example.business.abstracts.ICategoryService;
import com.example.core.exception.NotFoundException;
import com.example.core.utils.Message;
import com.example.dao.BookRepo;
import com.example.dao.CategoryRepo;
import com.example.entities.Author;
import com.example.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryManager implements ICategoryService {
    private final CategoryRepo categoryRepo;
    private final BookRepo bookRepo;

    public CategoryManager(CategoryRepo categoryRepo, BookRepo bookRepo) {
        this.categoryRepo = categoryRepo;
        this.bookRepo = bookRepo;
    }

    @Override
    public Category save(Category category) {
        return this.categoryRepo.save(category);
    }

    @Override
    public Category get(int id) {
        return this.categoryRepo.findById(id).orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));
    }

    @Override
    public Category update(Category category) {
        this.get(category.getId());
        return this.categoryRepo.save(category);
    }

    @Override
    public String delete(int id) {
        Category category = this.get(id);

        boolean hasBooks = bookRepo.existsByCategories_Id(id);
        if (hasBooks) {
            return "Bu kategoriye ait kitap var. Bu kategori silinemedi.";
        }

        this.categoryRepo.delete(category);
        return "Kategori silindi.";
    }

    @Override
    public Page<Category> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return this.categoryRepo.findAll(pageable);
    }
}
