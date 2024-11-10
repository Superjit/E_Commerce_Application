package com.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.entity.Order;
import com.spring.entity.OrderItem;
import com.spring.repo.OrderRepository;
import com.spring.servicesImpl.OrderItemRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/orders/items")
public class OrderItemsController {
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private OrderRepository orderRepository;

    @RequestMapping
    public List<OrderItem> getOrderItems() {
        return orderItemRepository.findAll();
    }

    // Update cart item quantity
    @PutMapping("/update/{itemId}")
    public ResponseEntity<Order> updateCartItem(@PathVariable Long itemId, @RequestParam int quantity) {
        try {
            OrderItem item = orderItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            item.setQuantity(quantity);
            orderItemRepository.save(item);

            // Update the total price in the order after modifying the quantity
            Order order = item.getOrder();
            Double updatedTotal = orderItemRepository.calculateTotalAmountForOrder(order.getId());
            order.setTotalAmount(updatedTotal != null ? updatedTotal : 0.0);
            orderRepository.save(order);

            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Remove a cart item
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Order> removeCartItem(@PathVariable Long itemId) {
        try {
            OrderItem item = orderItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            Order order = item.getOrder();
            
            orderItemRepository.delete(item);

            // Update total price after item removal
            Double updatedTotal = orderItemRepository.calculateTotalAmountForOrder(order.getId());
            order.setTotalAmount(updatedTotal != null ? updatedTotal : 0.0);
            orderRepository.save(order);

            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
