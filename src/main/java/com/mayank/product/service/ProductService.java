package com.mayank.product.service;

import com.mayank.product.dto.Product;
import com.mayank.product.exception.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<Product> getAllProducts() throws ProductNotFoundException;

    ResponseEntity<String> saveProduct(Product product);

    ResponseEntity<String> deleteProductById(String id);
}
