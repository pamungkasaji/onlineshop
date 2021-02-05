package com.pamungkasaji.beshop.service;

import com.pamungkasaji.beshop.entity.Order;
import com.pamungkasaji.beshop.entity.Payment;
import com.pamungkasaji.beshop.security.UserPrincipal;

public interface PaymentService {
    Payment processPayment(String id, Payment payment);
    void setPaymentStatus(String id, Payment payment);
    Order updatePaypal(String id, UserPrincipal currentUser, Payment paymentResult);
}
