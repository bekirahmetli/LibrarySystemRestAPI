package com.example.dao;

import com.example.entities.BookBorrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBorrowRepo extends JpaRepository<BookBorrowing,Integer> {
}
