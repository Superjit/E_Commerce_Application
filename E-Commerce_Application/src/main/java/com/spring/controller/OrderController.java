package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.spring.entity.Order;

import com.spring.services.OrderService;
import com.spring.servicesImpl.OrderItemRepository;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderItemRepository orderItemRepository;

    // Place order with cart items
//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable Long productId) {
    	
    	System.out.println(productId);
    	orderService.addToCart(productId);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    // Get all orders
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/items")
    public ResponseEntity<List<Order>> getAllOrders() {
    	System.out.println("items....");
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get a specific order by ID
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    // Cancel an order by ID
//    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok("Order cancelled successfully");
    }
    
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/count")
    public ResponseEntity<Long> getOrderCount() {
        Long count = orderItemRepository.count(); // Calling the service method to get the order count
        System.out.println("coart"+count);
        return ResponseEntity.ok(count); // Return the count as a response
    }
}
