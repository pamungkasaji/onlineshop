package com.pamungkasaji.beshop.model.response.order;

import com.pamungkasaji.beshop.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class OrderList {
    private List<Order> orders;
}
