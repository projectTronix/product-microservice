package com.mayank.product.service;

import com.mayank.product.dto.Category;
import com.mayank.product.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getAllCategories() throws ResourceNotFoundException;
    boolean saveCategory(Category category) throws Exception;
    String getCategoryIDByTitle(String title) throws Exception;
    Category getCategoryByTitle(String title) throws Exception;
    Category getCategoryById(String categoryId) throws Exception;
    boolean deleteCategoryById(String id) throws Exception;
}
