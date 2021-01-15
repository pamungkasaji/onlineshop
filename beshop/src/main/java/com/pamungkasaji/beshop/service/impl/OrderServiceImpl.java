package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.OrderItemEntity;
import com.pamungkasaji.beshop.entity.ShippingAddressEntity;
import com.pamungkasaji.beshop.exceptions.ResourceNotFoundException;
import com.pamungkasaji.beshop.repository.OrderRepository;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.OrderItemService;
import com.pamungkasaji.beshop.service.OrderService;
import com.pamungkasaji.beshop.service.ShippingAddressService;
import com.pamungkasaji.beshop.shared.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Optional<OrderEntity> getOrderById(Long id) {
        return orderRepository.findById(id);
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
    public OrderEntity updatePaid(Long id) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isEmpty()) throw new ResourceNotFoundException("Order is not found!");
        order.get().setPaid(true);

        return orderRepository.save(order.get());
    }

    @Override
    public OrderEntity updateDelivered(Long id) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isEmpty()) throw new ResourceNotFoundException("Order is not found!");
        order.get().setDelivered(true);

        return orderRepository.save(order.get());
    }
}
