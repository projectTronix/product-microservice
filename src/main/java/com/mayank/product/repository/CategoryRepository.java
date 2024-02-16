package com.mayank.product.repository;

import com.mayank.product.dto.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
