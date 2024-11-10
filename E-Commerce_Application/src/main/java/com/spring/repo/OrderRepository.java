package com.spring.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
//    List<Order> findByUserId(Long userId);
    
    Optional<Order> findByUserIdAndCompletedFalse(Long userId); // Find active cart by user
}
