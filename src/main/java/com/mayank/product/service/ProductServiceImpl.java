package com.mayank.product.service;

import com.mayank.product.dto.Product;
import com.mayank.product.exception.ProductNotFoundException;
import com.mayank.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Override
    public List<Product> getAllProducts() throws ProductNotFoundException {
        List<Product> products =  productRepository.findAll();
        if(products.isEmpty()) {
            throw new ProductNotFoundException("No products found.");
        }
        return products;
    }
    @Override
    public ResponseEntity<String> saveProduct(Product product) {
        try {
            productRepository.save(product);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> deleteProductById(String id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
