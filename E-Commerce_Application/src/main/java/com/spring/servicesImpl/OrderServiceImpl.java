package com.spring.servicesImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.spring.entity.Order;
import com.spring.entity.OrderItem;
import com.spring.entity.Product;
import com.spring.entity.User;
import com.spring.repo.OrderRepository;
 // Ensure this is imported
import com.spring.repo.ProductRepository;
import com.spring.repo.UserRepository;
import com.spring.services.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User user = userRepository.findByUsername(userDetails.getUsername())
                            .orElseThrow(() -> new RuntimeException("User not found"));
            return user.getId();
        } else {
            throw new RuntimeException("Authenticated user information not found.");
        }
    }

    @Transactional
    public int addToCart(Long productId) {
        Long userId = getAuthenticatedUserId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Order cart = orderRepository.findByUserIdAndCompletedFalse(userId)
                .orElseGet(() -> {
                    Order newCart = new Order();
                    newCart.setUser(userRepository.findById(userId).orElseThrow());
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setCompleted(false); // Ensure `completed` is set
                    return orderRepository.save(newCart);
                });

        OrderItem existingItem = cart.getOrderItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            existingItem.setPrice(product.getPrice() * existingItem.getQuantity());
            orderItemRepository.save(existingItem);
        } else {
            OrderItem newItem = new OrderItem();
            newItem.setProduct(product);
            newItem.setOrder(cart);
            newItem.setQuantity(1);
            newItem.setPrice(product.getPrice());
            cart.getOrderItems().add(newItem);
            orderItemRepository.save(newItem);
        }
        
        double totalAmount = cart.getOrderItems().stream()
                .mapToDouble(item -> item.getPrice())
                .sum();
        cart.setTotalAmount(totalAmount);
        cart.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(cart);
        return 1;
    }

    @Override
    public List<Order> getAllOrders() {
    	System.out.println(orderItemRepository.findAll());
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus("Cancelled");
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
        }
    }
    
    @Override
    public Long getOrderCount() {
        return orderRepository.count();
    }
}
