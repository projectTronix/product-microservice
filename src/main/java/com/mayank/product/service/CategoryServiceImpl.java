package com.mayank.product.service;

import com.mayank.product.dto.Category;
import com.mayank.product.exception.ResourceNotFoundException;
import com.mayank.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final LogManager logManager = LogManager.getLogManager();
    private final Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
    @Override
    public List<Category> getAllCategories() throws ResourceNotFoundException {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new ResourceNotFoundException("No Categories found.");
        }
        return categories;
    }

    @Override
    public boolean saveCategory(Category category) throws Exception {
        try {
            categoryRepository.save(category);
            return true;
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem in saveCategory in CategoryService - "+ e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getCategoryIDByTitle(String title) throws Exception {
        try {
            Optional<Category> opt = categoryRepository.findByTitle(title);
            if(opt.isEmpty()) {
                throw new ResourceNotFoundException("Invalid Category Title.");
            }
            return opt.get().getCategoryId();
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching Category ID from title in getCategoryIDByTitle in CategoryService - " + e.getMessage());
            throw new Exception("Encountered a problem while fetching Category ID from title.");
        }
    }

    @Override
    public Category getCategoryByTitle(String title) throws Exception {
        try {
            Optional<Category> opt = categoryRepository.findByTitle(title);
            if(opt.isEmpty()) {
                throw new ResourceNotFoundException("Invalid Category Title.");
            }
            return opt.get();
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching Category from title in getCategoryByTitle in CategoryService -" + e.getMessage());
            throw new Exception("Encountered a problem while fetching Category from title.");
        }
    }

    @Override
    public Category getCategoryById(String categoryId) throws Exception {
        try {
            Optional<Category> opt = categoryRepository.findById(categoryId);
            if(opt.isEmpty()) {
                throw new ResourceNotFoundException("Invalid Category Id.");
            }
            return opt.get();
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching Category from id in getCategoryById in CategoryService -" + e.getMessage());
            throw new Exception("Encountered a problem while fetching Category from id.");
        }
    }

    @Override
    public boolean deleteCategoryById(String id) throws Exception {
        try {
            boolean isEmpty = categoryRepository.findById(id).isEmpty();
            if(isEmpty) {
                throw new ResourceNotFoundException("No category found to delete.");
            }
            categoryRepository.deleteById(id);
            return categoryRepository.findById(id).isEmpty();
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem in deleteCategoryById in CategoryService " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}
