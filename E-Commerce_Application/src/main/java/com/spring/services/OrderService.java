package com.spring.services;

import java.util.List;

import com.spring.entity.Order;

public interface OrderService {
	int addToCart(Long productId);

	List<Order> getAllOrders();

	Order getOrderById(Long id);

	void cancelOrder(Long id);
	
	Long getOrderCount();

}
