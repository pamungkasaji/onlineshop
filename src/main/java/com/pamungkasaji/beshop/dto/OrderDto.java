package com.pamungkasaji.beshop.dto;

import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.OrderItemEntity;
import com.pamungkasaji.beshop.entity.ShippingAddressEntity;
import com.pamungkasaji.beshop.entity.UserEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Data
public class OrderDto implements Serializable {
    private static final long serialVersionUID = -6542715077410389418L;

    private String orderId;
    private List<OrderItemEntity> orderItems;
    private UserEntity user;
    private ShippingAddressEntity shippingAddress;
    private String userId;
    private boolean paid;
    private LocalDateTime paidAt;
    private boolean isDelivered;
    private BigDecimal shippingPrice;
    private BigDecimal totalPrice;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private String token;
    private String redirect_url;

    public static Map<String, Object> midtransCompleteRequest(OrderEntity newOrder){

        Map<String, Object> transDetail = new HashMap<>();
        transDetail.put("order_id", newOrder.getOrderId());
        transDetail.put("gross_amount", newOrder.getTotalPrice());

        List<Map<String, Object>> items = new ArrayList<>();

        for (OrderItemEntity orderItem : newOrder.getOrderItems()){
            Map<String,Object> item = new HashMap<>();
            item.put("id", orderItem.getProduct());
            item.put("price", orderItem.getPrice());
            item.put("quantity", orderItem.getQty());
            item.put("name", orderItem.getName());
            items.add(item);
        }

        // shipping added to orderItem
        Map<String,Object> shipping = new HashMap<>();
        shipping.put("id", newOrder.getShippingAddress().getId());
        shipping.put("price", newOrder.getShippingPrice());
        shipping.put("quantity", 1);
        shipping.put("name", "shipping to: " + newOrder.getShippingAddress().getCity());
        items.add(shipping);

        Map<String, Object> shipping_address = new HashMap<>();
        shipping_address.put("address", newOrder.getShippingAddress().getAddress());
        shipping_address.put("city", newOrder.getShippingAddress().getCity());
//        shipping_address.put("phone", "0928282828");
//        shipping_address.put("country_code", "IDN");

        Map<String, Object> custDetail = new HashMap<>();

//        custDetail.put("first_name", newOrder.getUser().getName());
//        custDetail.put("email", newOrder.getUser().getEmail());
//        custDetail.put("phone", newOrder.getUser().getPhone());
        custDetail.put("shipping_address", shipping_address);

        Map<String, Object> body = new HashMap<>();
//        if (creditCard != null) {
//            body.put("credit_card", creditCard);
//        }
        body.put("transaction_details", transDetail);
        body.put("item_details", items);
        body.put("customer_details", custDetail);

//        if (!paymentType.isEmpty()) {
//            body.put("payment_type", paymentType);
//        }

        return body;
    }

    public static Map<String, Object> midtransMinimumRequest(OrderEntity newOrder){
        Map<String, Object> midtransRequest = new HashMap<>();
        Map<String, Object> transDetail = new HashMap<>();
        transDetail.put("order_id", newOrder.getOrderId());
        transDetail.put("gross_amount", newOrder.getTotalPrice());
        midtransRequest.put("transaction_details", transDetail);
        return midtransRequest;
    }
}
