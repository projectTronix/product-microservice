package com.mayank.product.service;

import com.mayank.product.dto.Category;
import com.mayank.product.exception.ProductNotFoundException;
import com.mayank.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() throws ProductNotFoundException {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new ProductNotFoundException("No Categories found.");
        }
        return categories;
    }

    @Override
    public ResponseEntity<String> saveCategory(Category category) {
        try {
            categoryRepository.save(category);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String getCategoryIDByTitle(String title) {
        try {
            Optional<Category> opt = categoryRepository.findByTitle(title);
            if(opt.isEmpty()) {
                throw new ProductNotFoundException("Invalid Category Title.");
            }
            return opt.get().getCategoryId();
        } catch(ProductNotFoundException e) {
            return e.getMessage();
        } catch(Exception e) {
            return "Encountered a problem while fetching Category ID from title.";
        }
    }

    @Override
    public Optional<Category> getCategoryByTitle(String title) throws ProductNotFoundException {
        Optional<Category> opt = categoryRepository.findByTitle(title);
        if(opt.isEmpty()) {
            throw new ProductNotFoundException("Invalid Category Title.");
        }
        return opt;
    }

}
