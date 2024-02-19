package com.mayank.product.controller;

import com.mayank.product.dto.Product;
import com.mayank.product.exception.ProductNotFoundException;
import com.mayank.product.service.CategoryService;
import com.mayank.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("products/")
@Validated
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final LogManager logManager = LogManager.getLogManager();
    private final Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
        } catch(ProductNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.CONFLICT);
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> postProduct(@RequestBody Product product) {
        try {
            String title = product.getTitle();
            String description = product.getDescription();
            String imgUrl = product.getImageUrl();
            Integer price = product.getPrice();
            String categoryID = categoryService.getCategoryIDByTitle(product.getCategoryID());
            product.setCategoryID(categoryID);
            if(title.isBlank() || description.isBlank() || imgUrl.isBlank()) {
                logger.log(Level.WARNING, "Title or Description or Image URL is Empty.");
                throw new Exception("Title or Description or Image URL is Empty.");
            }
            ResponseEntity<String> response = productService.saveProduct(product);
            if(response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                logger.log(Level.WARNING, "Product already exists.");
                throw new Exception("Product already exists.");
            }
            logger.log(Level.INFO, "Product added Successfully.");
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch(Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") String id) {
        try {
            ResponseEntity<String> response = productService.deleteProductById(id);
            if(response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                logger.log(Level.WARNING, "Product with given ID doesn't exists.");
                throw new Exception("Product with given ID doesn't exists.");
            }
            logger.log(Level.INFO, "Product deleted Successfully.");
            return new ResponseEntity<>("Product deleted Successfully.", HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
