package com.mayank.product.service;

import com.mayank.product.dto.Category;
import com.mayank.product.exception.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {
    List<Category> getAllCategories() throws ProductNotFoundException;
    ResponseEntity<String> saveCategory(Category category);
    String getCategoryIDByTitle(String title);
    Optional<Category> getCategoryByTitle(String title) throws ProductNotFoundException;
}
