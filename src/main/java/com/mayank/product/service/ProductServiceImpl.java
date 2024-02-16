package com.mayank.product.service;

import com.mayank.product.dto.Product;
import com.mayank.product.exception.ProductNotFoundException;
import com.mayank.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Override
    public List<Product> getAllProducts() throws ProductNotFoundException {
        List<Product> products =  productRepository.findAll();
        if(products.isEmpty()) {
            throw new ProductNotFoundException("No products found.");
        }
        return products;
    }
    public ResponseEntity<String> saveProduct(Product product) {
        try {
            productRepository.save(product);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
