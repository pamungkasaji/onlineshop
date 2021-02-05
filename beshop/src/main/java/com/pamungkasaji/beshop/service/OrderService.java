package com.pamungkasaji.beshop.service;

import com.midtrans.httpclient.error.MidtransError;
import com.pamungkasaji.beshop.entity.Order;
import com.pamungkasaji.beshop.security.UserPrincipal;

import java.util.List;

public interface OrderService {
    Order getOrderById(String id);
    List<Order> getMyOrders(UserPrincipal currentUser);
    List<Order> getAllOrders();
    Order createOrder(UserPrincipal currentUser, Order newOrder) throws MidtransError;
    Order updateDelivered(String id);
}
