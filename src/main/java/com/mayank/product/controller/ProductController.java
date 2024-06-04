package com.mayank.product.controller;

import com.mayank.product.dto.Category;
import com.mayank.product.dto.CustomResponse;
import com.mayank.product.dto.Product;
import com.mayank.product.service.CategoryService;
import com.mayank.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("products/")
@Validated
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final LogManager logManager = LogManager.getLogManager();
    private final Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

//    @GetMapping("/all")
//    public ResponseEntity<List<Product>> getAllProducts() {
//        try {
//            List<Product> products = productService.getAllProducts();
//            logger.log(Level.INFO, "Products fetched successfully.");
//            return new ResponseEntity<>(products, HttpStatus.OK);
//        } catch(Exception e) {
//            logger.log(Level.WARNING, "Encountered a problem while fetching products -- getAllProducts in ProductController. - " + e.getMessage());
//            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
//        }
//    }
    @PostMapping("/add")
    public CustomResponse postProduct(@RequestBody Product product) {
        try {
            String title = product.getTitle();
            String description = product.getDescription();
            String imgUrl = product.getImageUrl();
            Integer price = product.getPrice();
            String categoryTitle = product.getCategoryTitle();
            if(price <= 0) {
                logger.log(Level.WARNING, "Price cannot be less than 0.");
                throw new Exception("Price cannot be less than 0..");
            }
            String categoryID = categoryService.getCategoryIDByTitle(product.getCategoryID());
            product.setCategoryID(categoryID);
            if(title.isBlank() || description.isBlank() || imgUrl.isBlank() || categoryTitle.isBlank()) {
                logger.log(Level.WARNING, "Title or Description or Image URL or CategoryTitle is Empty.");
                throw new Exception("Title or Description or Image URL or CategoryTitle is Empty.");
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
    @DeleteMapping("/delete/{id}")
    public CustomResponse deleteProduct(@PathVariable("id") String id) {
        try {
            if(id.isBlank()) {
                throw new Exception("No product id provided");
            }
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
    @GetMapping("/all")
    public ResponseEntity<Page<Product>> searchProduct(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "1") Integer direction,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "6") Integer size
    ) {
        try {
            Pageable pageable;
            if(sortBy != null && !sortBy.isEmpty()) {
                Sort.Direction sortDirection = (direction == 1) ? Sort.Direction.ASC : Sort.Direction.DESC;
                pageable = PageRequest.of(page,size, Sort.by(sortDirection, sortBy));
            }
            else {
                pageable = PageRequest.of(page,size);
            }
            return new ResponseEntity<>(productService.search(name, category, minPrice, maxPrice, pageable), HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching products -- getProductsSortedByPrice in ProductController. - " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") String id) {
        try {
            Product product = productService.getProductById(id);
            logger.log(Level.INFO, "Product fetched Successfully.");
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching product -- getProductById in ProductController : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/category/{categoryTitle}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("categoryTitle") String categoryTitle) {
        try {
            List<Product> productList = productService.getProductsByCategoryId(categoryService.getCategoryIDByTitle(categoryTitle));
            logger.log(Level.INFO, "Products by Category fetched Successfully.");
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching products by category -- getProductsByCategory in ProductController : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
