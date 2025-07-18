package com.example.openai.chatgpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.openai.chatgpt.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
