package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.dto.UserDto;
import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.OrderItemEntity;
import com.pamungkasaji.beshop.entity.PaymentResultEntity;
import com.pamungkasaji.beshop.entity.ShippingAddressEntity;
import com.pamungkasaji.beshop.exceptions.OrderServiceException;
import com.pamungkasaji.beshop.exceptions.ResourceNotFoundException;
import com.pamungkasaji.beshop.repository.OrderRepository;
import com.pamungkasaji.beshop.repository.PaymentRepository;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.OrderItemService;
import com.pamungkasaji.beshop.service.OrderService;
import com.pamungkasaji.beshop.service.ShippingAddressService;
import com.pamungkasaji.beshop.shared.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    ShippingAddressService shippingAddressService;

    PaymentRepository paymentRepository;

    OrderItemService orderItemService;

    Utils utils;

    public OrderServiceImpl(OrderRepository orderRepository, ShippingAddressService shippingAddressService,
                            PaymentRepository paymentRepository, OrderItemService orderItemService, Utils utils) {
        this.shippingAddressService = shippingAddressService;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.utils = utils;
    }

    @Override
    public OrderEntity getOrderById(String id) {
        return orderRepository.findByOrderId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order is not found!"));
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
    public OrderEntity createOrder(UserPrincipal currentUser, OrderEntity newOrder) {

        for(int i=0;i<newOrder.getOrderItems().size();i++)
        {
            OrderItemEntity orderItem = newOrder.getOrderItems().get(i);
            newOrder.getOrderItems().set(i, orderItem);
            orderItem.setOrder(newOrder);
        }

        newOrder.setShippingAddress(newOrder.getShippingAddress());

        newOrder.setUserId(currentUser.getUserId());

        return orderRepository.save(newOrder);
    }

    @Override
    public OrderEntity updatePaid(String id, UserPrincipal currentUser, PaymentResultEntity paymentResult) {
        OrderEntity order = getOrderById(id);

        if (!order.getUserId().equals(currentUser.getUserId())){
            throw new OrderServiceException(HttpStatus.UNAUTHORIZED, "You don't have access to this order");
        }
        if (paymentResult.getStatus().equals("COMPLETED")){
            order.setPaid(true);
        }
        paymentRepository.save(paymentResult);
        order.setPaymentResult(paymentResult);
        order.setPaidAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Override
    public OrderEntity updateDelivered(String id) {
        OrderEntity order = getOrderById(id);
        order.setDelivered(true);

        return orderRepository.save(order);
    }
}
