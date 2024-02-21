package com.mayank.product.service;

import com.mayank.product.dto.Product;
import com.mayank.product.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<Product> getAllProducts() throws Exception;

    boolean saveProduct(Product product) throws Exception;

    boolean deleteProductById(String id) throws Exception;

    List<Product> getProductsSortedByPrice(boolean asc) throws Exception;

    List<Product> getProductsSortedByName(boolean asc) throws Exception;
}
