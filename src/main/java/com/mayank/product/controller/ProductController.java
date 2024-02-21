package com.mayank.product.controller;

import com.mayank.product.dto.CustomResponse;
import com.mayank.product.dto.Product;
import com.mayank.product.exception.ResourceNotFoundException;
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
            List<Product> products = productService.getAllProducts();
            logger.log(Level.INFO, "Products fetched successfully.");
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching products -- getAllProducts in ProductController. - " + e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }
    @PostMapping("/add")
    public CustomResponse postProduct(@RequestBody Product product) {
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
            boolean status = productService.saveProduct(product);
            if(!status) throw new Exception();
            logger.log(Level.INFO, "Product added Successfully.");
            return new CustomResponse("Product added Successfully.", HttpStatus.CREATED);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while adding product -- postProduct in ProductController. - " + e.getMessage());
            return new CustomResponse("Encountered a problem while adding product.", HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    public CustomResponse deleteProduct(@PathVariable("id") String id) {
        try {
            boolean status = productService.deleteProductById(id);
            if(!status) {
                throw new Exception();
            }
            logger.log(Level.INFO, "Product deleted Successfully.");
            return new CustomResponse("Product Deleted Successfully.", HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while deleting product -- deleteProductById in ProductController. - " + e.getMessage());
            return new CustomResponse("Encountered a problem while deleting the product.", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/sortedByPrice")
    public ResponseEntity<List<Product>> getProductsSortedByPrice(@RequestParam boolean asc) {
        try {
            List<Product> products = productService.getProductsSortedByPrice(asc);
            logger.log(Level.INFO, "Sorted Products fetched successfully.");
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching products -- getProductsSortedByPrice in ProductController. - " + e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }
    @GetMapping("/sortedByName")
    public ResponseEntity<List<Product>> getProductsSortedByName(@RequestParam boolean asc) {
        try {
            List<Product> products = productService.getProductsSortedByName(asc);
            logger.log(Level.INFO, "Sorted Products fetched successfully.");
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching products -- getProductsSortedByName in ProductController. - " + e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }
}
