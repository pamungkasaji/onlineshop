package com.pamungkasaji.beshop.service;

import com.pamungkasaji.beshop.entity.OrderItemEntity;
import java.util.Set;

public interface OrderItemService {
    void save(Set<OrderItemEntity> orderItem);
}
