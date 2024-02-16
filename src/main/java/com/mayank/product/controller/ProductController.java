package com.mayank.product.controller;

import com.mayank.product.dto.Product;
import com.mayank.product.exception.ProductNotFoundException;
import com.mayank.product.service.CategoryService;
import com.mayank.product.service.ProductService;
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
@RequestMapping("product")
@Validated
public class ProductController {
    ProductService productService;
    CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    LogManager logManager = LogManager.getLogManager();
    Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
        } catch(ProductNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.CONFLICT);
        }
    }
    //    @GetMapping("/id/{id}")
//    public ResponseEntity<Product> getById(@PathVariable @Valid @NotBlank(message="ysdhjs") String id) {
//        return new ResponseEntity<>(null);
//    }
    @PostMapping("/add")
    public ResponseEntity<?> postProduct(@RequestBody Product product) {
        try {
            String title = product.getTitle();
            String description = product.getDescription();
            String imgUrl = product.getImageUrl();
            Integer price = product.getPrice();
            String categoryID = product.getCategoryID();
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
}
