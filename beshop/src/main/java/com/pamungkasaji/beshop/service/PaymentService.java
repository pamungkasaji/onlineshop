package com.pamungkasaji.beshop.service;

import com.pamungkasaji.beshop.entity.Order;
import com.pamungkasaji.beshop.entity.Payment;
import com.pamungkasaji.beshop.entity.Product;
import com.pamungkasaji.beshop.model.request.midtrans.TransDetail;
import com.pamungkasaji.beshop.security.UserPrincipal;

import java.util.Optional;

public interface PaymentService {
    Order setPaymentStatus(String id, TransDetail transDetail);
}
