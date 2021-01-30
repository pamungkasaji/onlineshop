package com.pamungkasaji.beshop.service;

import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.PaymentEntity;
import com.pamungkasaji.beshop.security.UserPrincipal;

public interface PaymentService {
    PaymentEntity processPayment(String id, PaymentEntity payment);
    void setPaymentStatus(String id, PaymentEntity payment);
    OrderEntity updatePaypal(String id, UserPrincipal currentUser, PaymentEntity paymentResult);
}
