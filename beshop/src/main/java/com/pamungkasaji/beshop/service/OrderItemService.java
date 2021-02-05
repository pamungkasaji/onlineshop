package com.pamungkasaji.beshop.service;

import com.pamungkasaji.beshop.entity.OrderItem;
import java.util.Set;

public interface OrderItemService {
    void save(Set<OrderItem> orderItem);
}
