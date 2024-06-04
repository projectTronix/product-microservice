package com.mayank.product.service;

import com.mayank.product.dto.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<Product> getAllProducts() throws Exception;
    boolean saveProduct(Product product) throws Exception;
    boolean deleteProductById(String id) throws Exception;
    // -1 for desc and 1 for asc
    Page<Product> search(String name, String category, Integer minPrice, Integer maxPrice, Pageable pageable) throws Exception;
    Product getProductById(String id) throws Exception;
    List<Product> getProductsByCategoryId(String categoryId) throws Exception;
}
