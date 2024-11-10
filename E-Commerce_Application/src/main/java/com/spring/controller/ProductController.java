package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.entity.Product;
import com.spring.repo.ProductRepository;
import com.spring.services.ProductService;
import com.spring.servicesImpl.OrderItemRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
//        List<Product> products = productService.getAllProducts();
        List<Product> products = productRepository.findAllAvailableProducts();
    	
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
    	System.out.println(product);
    	product.setAvailable(true);
        productService.addProduct(product);
        return ResponseEntity.ok("Product added successfully");
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        productService.updateProduct(id, product);
        return ResponseEntity.ok("Product updated successfully");
    }

//    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
//        productService.deleteProduct(id);
//        return ResponseEntity.ok("Product deleted successfully");
//    }
    
 // Mark the product as inactive instead of deleting
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void markProductAsInactive(@PathVariable("id") Long productId) {
    	System.out.println(productId);
        Product product = productRepository.findById(productId).orElseThrow();
        product.setAvailable(false);
        productRepository.save(product);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allProducts")
    public ResponseEntity<List<Product>> getAllProductsForAdmin() {
        List<Product> products = productService.getAllProducts();
    	
        return ResponseEntity.ok(products);
    }
}