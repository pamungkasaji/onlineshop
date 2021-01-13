package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.ShippingAddressEntity;
import com.pamungkasaji.beshop.repository.OrderRepository;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.OrderItemService;
import com.pamungkasaji.beshop.service.OrderService;
import com.pamungkasaji.beshop.service.ShippingAddressService;
import com.pamungkasaji.beshop.shared.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    ShippingAddressService shippingAddressService;

    OrderItemService orderItemService;

    Utils utils;

    public OrderServiceImpl(OrderRepository orderRepository, ShippingAddressService shippingAddressService,
                            OrderItemService orderItemService, Utils utils) {
        this.shippingAddressService = shippingAddressService;
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.utils = utils;
    }

    @Override
    public Optional<OrderEntity> getOrderByOrderId(String id) {
        return Optional.empty();
    }

    @Override
    public Page<OrderEntity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public OrderEntity createOrder(UserPrincipal currentUser, OrderEntity newOrder) {

//        shippingAddressService.save(newOrder.getShippingAddress());
        ShippingAddressEntity shippingAddress = newOrder.getShippingAddress();
        shippingAddress.setOrder(newOrder);
        shippingAddressService.save(shippingAddress);

//        String generateOrderId = utils.generateId(25);
//        newOrder.setUserid(currentUser.getUserId());

        newOrder.setShippingAddress(shippingAddress);

//        orderItemService.save(newOrder.getOrderItems());

        return orderRepository.save(newOrder);
    }

}
