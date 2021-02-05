package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.entity.OrderItem;
import com.pamungkasaji.beshop.repository.OrderItemRepository;
import com.pamungkasaji.beshop.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Override
    public void save(Set<OrderItem> orderItem) {
        orderItem.forEach(orderItemRepository::save);
    }
}
