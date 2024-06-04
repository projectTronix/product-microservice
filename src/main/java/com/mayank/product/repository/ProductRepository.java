package com.mayank.product.repository;

import com.mayank.product.dto.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<List<Product>> findByCategoryID(String categoryId);
}
