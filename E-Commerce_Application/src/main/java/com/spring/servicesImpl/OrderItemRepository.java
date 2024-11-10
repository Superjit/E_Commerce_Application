package com.spring.servicesImpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
	
	 @Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItem oi WHERE oi.order.id = :orderId")
	    Double calculateTotalAmountForOrder(@Param("orderId") Long orderId);

}
