package com.mayank.product.service;

import com.mayank.product.dto.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    public ResponseEntity<String> saveCategory(Category category);
}
