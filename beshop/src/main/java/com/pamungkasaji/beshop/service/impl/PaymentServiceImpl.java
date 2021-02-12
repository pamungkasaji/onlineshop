package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.constant.FraudStatus;
import com.pamungkasaji.beshop.constant.TransactionStatus;
import com.pamungkasaji.beshop.controller.PaymentController;
import com.pamungkasaji.beshop.entity.Order;
import com.pamungkasaji.beshop.entity.Payment;
import com.pamungkasaji.beshop.exceptions.OrderServiceException;
import com.pamungkasaji.beshop.exceptions.ResourceNotFoundException;
import com.pamungkasaji.beshop.model.request.midtrans.TransDetail;
import com.pamungkasaji.beshop.repository.OrderRepository;
import com.pamungkasaji.beshop.repository.PaymentRepository;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    OrderRepository orderRepository;

    PaymentRepository paymentRepository;

    public PaymentServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Order setPaymentStatus(String id, TransDetail transDetail) {
        Order order = getOrderEntity(id);

        LOGGER.info("Transaction notification received. Order ID: " + id + ". Transaction status: " +
                transDetail.getTransactionStatus() + ". Fraud status: " + transDetail.getFraudStatus());

        if (transDetail.getTransactionStatus().equals("capture")) {
            if (transDetail.getFraudStatus().equals("challenge")) {
                order.getPayment().setTransactionStatus(TransactionStatus.CAPTURE);
                order.getPayment().setFraudStatus(FraudStatus.CHALLENGE);
            } else if (transDetail.getFraudStatus().equals("accept")) {
                order.getPayment().setPaid(true);
                order.getPayment().setTransactionStatus(TransactionStatus.CAPTURE);
                order.getPayment().setFraudStatus(FraudStatus.ACCEPT);
                order.getPayment().setPaymentType(transDetail.getPaymentType());
                try {
                    order.getPayment().setPaidAt(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(transDetail.getTransactionTime()));
                } catch (ParseException ignored){

                }
            }
        } else if (transDetail.getTransactionStatus().equals("settlement")){
            order.getPayment().setPaid(true);
            order.getPayment().setPaymentType(transDetail.getPaymentType());
            order.getPayment().setTransactionStatus(TransactionStatus.SETTLEMENT);
            try {
                order.getPayment().setPaidAt(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(transDetail.getTransactionTime()));
            } catch (ParseException ignored){

            }
        } else {
            order.getPayment().setTransactionStatus(TransactionStatus.fromString(transDetail.getTransactionStatus()));
            order.getPayment().setFraudStatus(FraudStatus.fromString(transDetail.getFraudStatus()));
        }

        order.getPayment().setTransactionId(transDetail.getTransactionId());

        return orderRepository.save(order);

//        if (payment.getTransactionStatus().equals(TransactionStatus.CAPTURE.status) || payment.getTransactionStatus().equals("settlement")) {
//            order.getPayment().setPaid(true);
//            order.getPayment().setPaidAt(LocalDateTime.now());
//            orderRepository.save(order);
//        }
//        paymentRepository.save(payment);
    }

    private Order getOrderEntity(String id) {
        Optional<Order> orderOptional = orderRepository.findByOrderId(id);
        if (!orderOptional.isPresent()) throw new ResourceNotFoundException("Order is not found!");
        return orderOptional.get();
    }

    // PAYPAL
//    @Override
//    public Order updatePaypal(String id, UserPrincipal currentUser, Payment payment) {
////        OrderEntity order = getOrderById(id);
//        Order order = getOrderEntity(id);
//
//        if (!order.getUserId().equals(currentUser.getUserId())){
//            throw new OrderServiceException(HttpStatus.UNAUTHORIZED, "You don't have access to this order");
//        }
//        // set status to lower case
//        payment.setTransactionStatus(payment.getTransactionStatus().toLowerCase());
//
//        if (payment.getTransactionStatus().equals("completed")) {
//            order.getPayment().setPaid(true);
//            order.getPayment().setPaidAt(LocalDateTime.now());
//        }
//        paymentRepository.save(payment);
//
//        return orderRepository.save(order);
//    }
}
