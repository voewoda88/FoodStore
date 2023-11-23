package com.boots.service;

import com.boots.entity.Product;
import com.boots.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton")
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> allProducts() {
        return productRepository.findAll();
    }

    public boolean saveProduct(Product product) {
        Product productFromDB = productRepository.findByName(product.getName());

        if(productFromDB != null) {
            return false;
        }

        productRepository.save(product);
        return true;
    }

    public Long getLastProductId() {
        Product product = productRepository.findFirstByOrderByIdDesc();
        if(product == null) {
            return Math.round(0.0);
        }

        return product.getId();
    }

    public boolean deleteProduct(Long productId) {
        if(productRepository.findById(productId).isPresent()){
            productRepository.deleteById(productId);
            return true;
        }

        return false;
    }

    public Product findProductById(Long id) {
        Optional<Product> productFromDB = productRepository.findById(id);
        return productFromDB.orElse(new Product());
    }
}
