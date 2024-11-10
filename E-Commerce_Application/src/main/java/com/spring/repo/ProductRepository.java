package com.spring.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	 @Query("SELECT p FROM Product p WHERE p.isAvailable = true")
	    List<Product> findAllAvailableProducts();
}