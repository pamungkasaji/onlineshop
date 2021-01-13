package com.pamungkasaji.beshop.service;

import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.ProductEntity;
import com.pamungkasaji.beshop.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Optional;

public interface OrderService {
    Optional<OrderEntity> getOrderByOrderId(String id);
    Page<OrderEntity> findAll(Pageable pageable);
    OrderEntity createOrder(UserPrincipal currentUser, OrderEntity newOrder);
}
