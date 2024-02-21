package com.mayank.product.service;

import com.mayank.product.dto.Product;
import com.mayank.product.exception.ResourceNotFoundException;
import com.mayank.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final LogManager logManager = LogManager.getLogManager();
    private final Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
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
            productRepository.deleteById(id);
            boolean status = productRepository.findById(id).isEmpty();
            if(!status) {
                return false;
            }
            return true;
        } catch(Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Product> getProductsSortedByPrice(boolean asc) throws Exception {
        try {
            Sort.Direction sortDirection = Sort.Direction.ASC;
            if(!asc) sortDirection = Sort.Direction.DESC;
            List<Product> products = productRepository.findAll(Sort.by(sortDirection, "price"));
            if(products.isEmpty()) {
                throw new ResourceNotFoundException("No products found.");
            }
            return products;
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem in getProductsSorted in ProductService " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Product> getProductsSortedByName(boolean asc) throws Exception {
        try {
            Sort.Direction sortDirection = Sort.Direction.ASC;
            if(!asc) sortDirection = Sort.Direction.DESC;
            List<Product> products = productRepository.findAll(Sort.by(sortDirection, "title"));
            if(products.isEmpty()) {
                throw new ResourceNotFoundException("No products found.");
            }
            return products;
        } catch(Exception e) {
            logger.log(Level.WARNING, "Encountered a problem in getProductsSortedByName in ProductService " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
