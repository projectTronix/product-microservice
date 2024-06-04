package com.mayank.product.controller;

import com.mayank.product.dto.Category;
import com.mayank.product.dto.CustomResponse;
import com.mayank.product.exception.ResourceNotFoundException;
import com.mayank.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@CrossOrigin("*")
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
            List<Category> categories = categoryService.getAllCategories();
            logger.log(Level.INFO, "Categories fetched successfully.");
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch(ResourceNotFoundException e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching categories in CategoryController. - " + e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }
    @PostMapping("/add")
    public CustomResponse postCategory(@RequestBody Category category) {
        try {
            String title = category.getTitle();
            String description = category.getDescription();
            String imgUrl = category.getCategoryImageUrl();
            if(title.isBlank() || description.isBlank() || imgUrl.isBlank()) {
                logger.log(Level.WARNING, "Title or Description or Image URL is Empty.");
                throw new Exception("Title or Description or Image URL is Empty.");
            }
            boolean status = categoryService.saveCategory(category);
            if(!status) throw new Exception();
            logger.log(Level.INFO, "Category added Successfully.");
            return new CustomResponse("Category added Successfully.", HttpStatus.CREATED);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while adding category -- postCategory in CategoryController. - " + e.getMessage());
            return new CustomResponse("Encountered a problem while adding category.", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{title}")
    public ResponseEntity<?> getCategoryByTitle(@PathVariable("title") String title) {
        try {
            Category category = categoryService.getCategoryByTitle(title);
            logger.log(Level.INFO, "Category fetched Successfully.");
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching category -- getCategoryByTitle in CategoryController : " + e.getMessage());
            return new ResponseEntity<>("Encountered a problem while fetching category.", HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{title}")
    public CustomResponse deleteCategory(@PathVariable("title") String title) {
        try {
            if(title.isBlank()) {
                throw new Exception("No category title provided");
            }
            boolean status = categoryService.deleteCategoryById(categoryService.getCategoryIDByTitle(title));
            if(!status) {
                throw new Exception();
            }
            logger.log(Level.INFO, "Category deleted Successfully.");
            return new CustomResponse("Category Deleted Successfully.", HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while deleting Category -- deleteCategoryById in CategoryController. - " + e.getMessage());
            return new CustomResponse("Encountered a problem while deleting the Category.", HttpStatus.BAD_REQUEST);
        }
    }
}
