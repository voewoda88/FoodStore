package com.boots.repository;

import com.boots.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String productName);
    Product findFirstByOrderByIdDesc();
}
