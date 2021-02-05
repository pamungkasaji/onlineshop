package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.entity.Order;
import com.pamungkasaji.beshop.entity.Payment;
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
    public Payment processPayment(String id, Payment payment) {
        Order order = getOrderEntity(id);
        

        return null;
    }

    @Override
    public void setPaymentStatus(String id, Payment payment) {
        Order order = getOrderEntity(id);

        if (payment.getStatus().equals("success")) {
            order.getPayment().setPaid(true);
            order.getPayment().setPaidAt(LocalDateTime.now());
            orderRepository.save(order);
        }
        paymentRepository.save(payment);
    }

    private Order getOrderEntity(String id) {
        Optional<Order> orderOptional = orderRepository.findByOrderId(id);
        if (!orderOptional.isPresent()) throw new ResourceNotFoundException("Order is not found!");
        return orderOptional.get();
    }

    // PAYPAL
    @Override
    public Order updatePaypal(String id, UserPrincipal currentUser, Payment payment) {
//        OrderEntity order = getOrderById(id);
        Order order = getOrderEntity(id);

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
