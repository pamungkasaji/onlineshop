package com.pamungkasaji.beshop.repository;

import com.pamungkasaji.beshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
