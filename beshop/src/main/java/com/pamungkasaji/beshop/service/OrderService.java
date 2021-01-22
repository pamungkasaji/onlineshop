package com.pamungkasaji.beshop.service;

import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.PaymentResultEntity;
import com.pamungkasaji.beshop.entity.ProductEntity;
import com.pamungkasaji.beshop.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderEntity getOrderById(String id);
    List<OrderEntity> getMyOrders(UserPrincipal currentUser);
    List<OrderEntity> getAllOrders();
    OrderEntity createOrder(UserPrincipal currentUser, OrderEntity newOrder);
    OrderEntity updatePaid(String id, UserPrincipal currentUser, PaymentResultEntity paymentResult);
    OrderEntity updateDelivered(String id);
}
