package com.mayank.product.controller;

import com.mayank.product.dto.Category;
import com.mayank.product.service.CategoryService;
import com.mayank.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@RequestMapping("category")
public class CategoryController {
    private CategoryService categoryService;
    private ProductService productService;
    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
    @Autowired
    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }
    @PostMapping("/add")
    public ResponseEntity<?> postCategory(@RequestBody Category category) {
        try {
            String title = category.getTitle();
            String description = category.getDescription();
            String imgUrl = category.getCategoryImageUrl();
            if(title.isBlank() || description.isBlank() || imgUrl.isBlank()) {
                logger.log(Level.WARNING, "Title or Description or Image URL is Empty.");
                throw new Exception("Title or Description or Image URL is Empty.");
            }
            ResponseEntity<String> response = categoryService.saveCategory(category);
            if(response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                logger.log(Level.WARNING, "Category already exists.");
                throw new Exception("Category already exists.");
            }
            logger.log(Level.INFO, "Category added Successfully.");
            return new ResponseEntity<>(category, HttpStatus.CREATED);
        } catch(Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
