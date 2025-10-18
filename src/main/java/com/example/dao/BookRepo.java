package com.example.dao;

import com.example.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book,Integer> {
    boolean existsByCategories_Id(int categoryId);
}
