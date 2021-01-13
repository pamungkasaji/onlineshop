package com.pamungkasaji.beshop.repository;

import com.pamungkasaji.beshop.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

}
