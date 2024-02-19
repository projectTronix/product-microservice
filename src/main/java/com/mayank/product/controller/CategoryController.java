package com.mayank.product.controller;

import com.mayank.product.dto.Category;
import com.mayank.product.exception.ProductNotFoundException;
import com.mayank.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final LogManager logManager = LogManager.getLogManager();
    private final Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
        } catch(ProductNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.CONFLICT);
        }
    }
    @PostMapping("/add")
    public ResponseEntity<String> postCategory(@RequestBody Category category) {
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
            return new ResponseEntity<>("Category added Successfully.", HttpStatus.CREATED);
        } catch(Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{title}")
    public ResponseEntity<Category> getCategoryByTitle(@PathVariable("title") String title) {
        try {
            Optional<Category> category = categoryService.getCategoryByTitle(title);
            return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch(ProductNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
