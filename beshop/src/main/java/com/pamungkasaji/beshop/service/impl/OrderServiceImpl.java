package com.pamungkasaji.beshop.service.impl;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import com.pamungkasaji.beshop.dto.OrderDto;
import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.OrderItemEntity;
import com.pamungkasaji.beshop.entity.PaymentEntity;
import com.pamungkasaji.beshop.exceptions.OrderServiceException;
import com.pamungkasaji.beshop.exceptions.ResourceNotFoundException;
import com.pamungkasaji.beshop.repository.OrderRepository;
import com.pamungkasaji.beshop.repository.PaymentRepository;
import com.pamungkasaji.beshop.repository.UserRepository;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.OrderItemService;
import com.pamungkasaji.beshop.service.OrderService;
import com.pamungkasaji.beshop.shared.Utils;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public OrderEntity getOrderById(String id) {
        Optional<OrderEntity> orderOptional = orderRepository.findByOrderId(id);
        if (!orderOptional.isPresent()) throw new ResourceNotFoundException("Order is not found!");

        return orderOptional.get();
    }

    @Override
    public List<OrderEntity> getMyOrders(UserPrincipal currentUser) {
        return orderRepository.findByUserId(currentUser.getUserId());
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderEntity createOrder(UserPrincipal currentUser, OrderEntity newOrder) throws MidtransError{

        for(int i=0;i<newOrder.getOrderItems().size();i++) {
            OrderItemEntity orderItem = newOrder.getOrderItems().get(i);
            newOrder.getOrderItems().set(i, orderItem);
            orderItem.setOrder(newOrder);
        }

        newOrder.setShippingAddress(newOrder.getShippingAddress());
//        newOrder.setUser(currentUser.getUserId());
//        UserEntity user = userRepository.findByUserId(currentUser.getUserId());
        newOrder.setUserId(currentUser.getUserId());
        OrderEntity savedOrder = orderRepository.save(newOrder);

        if (!newOrder.getPaymentMethod().isEmpty()){
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
    public OrderEntity updateDelivered(String id) {
//        OrderEntity order = getOrderById(id);
        Optional<OrderEntity> orderOptional = orderRepository.findByOrderId(id);
        if (!orderOptional.isPresent()) throw new ResourceNotFoundException("Order is not found!");
        OrderEntity order = orderOptional.get();
        order.setDelivered(true);

        return orderRepository.save(order);
    }
}
