package com.pamungkasaji.beshop.service;

import com.midtrans.httpclient.error.MidtransError;
import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.PaymentEntity;
import com.pamungkasaji.beshop.security.UserPrincipal;

import java.util.List;

public interface OrderService {
    OrderEntity getOrderById(String id);
    List<OrderEntity> getMyOrders(UserPrincipal currentUser);
    List<OrderEntity> getAllOrders();
    OrderEntity createOrder(UserPrincipal currentUser, OrderEntity newOrder) throws MidtransError;
    void setPaymentStatus(String id, PaymentEntity payment);
    OrderEntity updatePaid(String id, UserPrincipal currentUser, PaymentEntity paymentResult);
    OrderEntity updateDelivered(String id);
}
