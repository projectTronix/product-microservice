package com.mayank.product.service;

import com.mayank.product.dto.Category;
import com.mayank.product.dto.Product;
import com.mayank.product.exception.ResourceNotFoundException;
import com.mayank.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final LogManager logManager = LogManager.getLogManager();
    private final Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final MongoTemplate mongoTemplate;
    @Override
    public List<Product> getAllProducts() throws Exception {
        try {
            List<Product> products =  productRepository.findAll();
            if(products.isEmpty()) {
                throw new ResourceNotFoundException("No products found.");
            }
            return products;
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem in getAllProducts in ProductService " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
    @Override
    public boolean saveProduct(Product product) throws Exception {
        try {
            productRepository.save(product);
            return true;
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem in saveProduct in ProductService - {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
    @Transactional
    @Override
    public boolean deleteProductById(String id) throws Exception {
        try {
            boolean isEmpty = productRepository.findById(id).isEmpty();
            if(isEmpty) {
                throw new ResourceNotFoundException("No product found to delete.");
            }
            productRepository.deleteById(id);
            return productRepository.findById(id).isEmpty();
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem in deleteProductById in ProductService " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
    @Override
    public Page<Product> search(String name, String category, Integer minPrice, Integer maxPrice, Pageable pageable) throws Exception {
        try {
            Query query = new Query().with(pageable);
            List<Criteria> criteria = new ArrayList<>();

            if(name !=null && !name.isEmpty()) {
                criteria.add(new Criteria().orOperator(Criteria.where("title").regex(name,"i"),Criteria.where("description").regex(name,"i")));
            }
            if(category !=null && !category.isEmpty()) {
                criteria.add(Criteria.where("categoryTitle").regex(category,"i"));
            }
            if(minPrice !=null && maxPrice !=null) {
                criteria.add(Criteria.where("price").gte(minPrice).lte(maxPrice));
            }
            if(!criteria.isEmpty()) {
                query.addCriteria(new Criteria()
                        .andOperator(criteria.toArray(new Criteria[0])));
            }

            Page<Product> products = PageableExecutionUtils.getPage(
                    mongoTemplate.find(query, Product.class
                    ), pageable, () -> mongoTemplate.count(query.skip(0).limit(0),Product.class));
            return products;
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem in getAllProducts in ProductService " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
    @Override
    public Product getProductById(String id) throws Exception {
        try {
            Optional<Product> opt = productRepository.findById(id);
            if(opt.isEmpty()) {
                throw new ResourceNotFoundException("Invalid Product ID.");
            }
            return opt.get();
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching Product from id in getProductById in ProductService -" + e.getMessage());
            throw new Exception("Encountered a problem while fetching Product from id.");
        }
    }

    @Override
    public List<Product> getProductsByCategoryId(String categoryId) throws Exception {
        try {
            Optional<List<Product>> opt = productRepository.findByCategoryID(categoryId);
            if(opt.isEmpty()) {
                throw new ResourceNotFoundException("No products found by Category Id.");
            }
            return opt.get();
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem while fetching Products by category in getProductById in ProductService -" + e.getMessage());
            throw new Exception("Encountered a problem while fetching Products by category.");
        }
    }
}
