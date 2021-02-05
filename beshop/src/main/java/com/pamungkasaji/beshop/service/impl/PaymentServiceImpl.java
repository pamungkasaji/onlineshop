package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.PaymentEntity;
import com.pamungkasaji.beshop.exceptions.OrderServiceException;
import com.pamungkasaji.beshop.exceptions.ResourceNotFoundException;
import com.pamungkasaji.beshop.repository.OrderRepository;
import com.pamungkasaji.beshop.repository.PaymentRepository;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    OrderRepository orderRepository;

    PaymentRepository paymentRepository;

    public PaymentServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentEntity processPayment(String id, PaymentEntity payment) {
        OrderEntity order = getOrderEntity(id);
        

        return null;
    }

    @Override
    public void setPaymentStatus(String id, PaymentEntity payment) {
        OrderEntity order = getOrderEntity(id);

        if (payment.getStatus().equals("success")) {
            order.getPayment().setPaid(true);
            order.getPayment().setPaidAt(LocalDateTime.now());
            orderRepository.save(order);
        }
        paymentRepository.save(payment);
    }

    private OrderEntity getOrderEntity(String id) {
        Optional<OrderEntity> orderOptional = orderRepository.findByOrderId(id);
        if (!orderOptional.isPresent()) throw new ResourceNotFoundException("Order is not found!");
        return orderOptional.get();
    }

    // PAYPAL
    @Override
    public OrderEntity updatePaypal(String id, UserPrincipal currentUser, PaymentEntity payment) {
//        OrderEntity order = getOrderById(id);
        OrderEntity order = getOrderEntity(id);

        if (!order.getUserId().equals(currentUser.getUserId())){
            throw new OrderServiceException(HttpStatus.UNAUTHORIZED, "You don't have access to this order");
        }
        // set status to lower case
        payment.setStatus(payment.getStatus().toLowerCase());

        if (payment.getStatus().equals("completed")) {
            order.getPayment().setPaid(true);
            order.getPayment().setPaidAt(LocalDateTime.now());
        }
        paymentRepository.save(payment);

        return orderRepository.save(order);
    }
}
