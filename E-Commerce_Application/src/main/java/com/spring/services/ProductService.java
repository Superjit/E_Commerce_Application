package com.spring.services;

import java.util.List;

import com.spring.entity.Product;

public interface ProductService {
	List<Product> getAllProducts();

	Product getProductById(Long id);

	Product addProduct(Product product);

	Product updateProduct(Long id, Product product);

	void deleteProduct(Long id);

}
