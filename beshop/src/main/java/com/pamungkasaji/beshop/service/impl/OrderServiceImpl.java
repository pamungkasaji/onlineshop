package com.pamungkasaji.beshop.service.impl;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import com.pamungkasaji.beshop.dto.OrderDto;
import com.pamungkasaji.beshop.entity.Order;
import com.pamungkasaji.beshop.entity.OrderItem;
import com.pamungkasaji.beshop.exceptions.ResourceNotFoundException;
import com.pamungkasaji.beshop.repository.OrderRepository;
import com.pamungkasaji.beshop.repository.PaymentRepository;
import com.pamungkasaji.beshop.repository.UserRepository;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.OrderItemService;
import com.pamungkasaji.beshop.service.OrderService;
import com.pamungkasaji.beshop.shared.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    PaymentRepository paymentRepository;

    OrderItemService orderItemService;

    UserRepository userRepository;

    Utils utils;

    @Value("${midtransClientKey}")
    private String midtransClientKey;

    public OrderServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository,
                            OrderItemService orderItemService, Utils utils) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.utils = utils;
    }

    private MidtransSnapApi snapApi = new ConfigFactory(
            new Config("SB-Mid-server-DIlYFfq4uoDp90490aR62jkJ",
                    "SB-Mid-client-1AvIQh0PMn9imV91",
                    false))
            .getSnapApi();

    @Override
    public Order getOrderById(String id) {
        Optional<Order> orderOptional = orderRepository.findByOrderId(id);
        if (!orderOptional.isPresent()) throw new ResourceNotFoundException("Order is not found!");

        return orderOptional.get();
    }

    @Override
    public List<Order> getMyOrders(UserPrincipal currentUser) {
        return orderRepository.findByUserId(currentUser.getUserId());
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order createOrder(UserPrincipal currentUser, Order newOrder) throws MidtransError{

        for(int i=0;i<newOrder.getOrderItems().size();i++) {
            OrderItem orderItem = newOrder.getOrderItems().get(i);
            newOrder.getOrderItems().set(i, orderItem);
            orderItem.setOrder(newOrder);
        }

        newOrder.setShipping(newOrder.getShipping());
        newOrder.setPayment(newOrder.getPayment());

        newOrder.setUserId(currentUser.getUserId());
        Order savedOrder = orderRepository.save(newOrder);

        if (!newOrder.getPayment().getPaymentMethod().isEmpty()){
            // Minimum request
//            JSONObject midtransResponse = snapApi.createTransaction(OrderDto.midtransMinimumRequest(savedOrder));

            // Complete request
            JSONObject midtransResponse = snapApi.createTransaction(OrderDto.midtransCompleteRequest(savedOrder));

            savedOrder.setToken(midtransResponse.get("token").toString());
            savedOrder.setRedirect_url(midtransResponse.get("redirect_url").toString());
        }

        return orderRepository.save(savedOrder);
    }

    @Override
    public Order updateDelivered(String id) {
//        OrderEntity order = getOrderById(id);
        Optional<Order> orderOptional = orderRepository.findByOrderId(id);
        if (!orderOptional.isPresent()) throw new ResourceNotFoundException("Order is not found!");
        Order order = orderOptional.get();
        order.getShipping().setDelivered(true);

        return orderRepository.save(order);
    }
}
