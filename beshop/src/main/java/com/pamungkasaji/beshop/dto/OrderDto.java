package com.pamungkasaji.beshop.dto;

import com.pamungkasaji.beshop.entity.Order;
import com.pamungkasaji.beshop.entity.OrderItem;
import com.pamungkasaji.beshop.entity.Shipping;
import com.pamungkasaji.beshop.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Data
public class OrderDto implements Serializable {
    private static final long serialVersionUID = -6542715077410389418L;

    private String orderId;
    private List<OrderItem> orderItems;
    private User user;
    private Shipping shippingAddress;
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

    public static Map<String, Object> midtransCompleteRequest(Order newOrder){

        Map<String, Object> transDetail = new HashMap<>();
        transDetail.put("order_id", newOrder.getOrderId());
        transDetail.put("gross_amount", newOrder.getItemsPrice().add(newOrder.getShipping().getShippingPrice()));

        List<Map<String, Object>> items = new ArrayList<>();

        for (OrderItem orderItem : newOrder.getOrderItems()){
            Map<String,Object> item = new HashMap<>();
            item.put("id", orderItem.getProduct());
            item.put("price", orderItem.getPrice());
            item.put("quantity", orderItem.getQty());
            item.put("name", orderItem.getName());
            items.add(item);
        }

        // shipping added to orderItem
        Map<String,Object> shipping = new HashMap<>();
        shipping.put("id", newOrder.getShipping().getId());
        shipping.put("price", newOrder.getShipping().getShippingPrice());
        shipping.put("quantity", 1);
        shipping.put("name", "shipping to: " + newOrder.getShipping().getCity());
        items.add(shipping);

        Map<String, Object> shipping_address = new HashMap<>();
        shipping_address.put("address", newOrder.getShipping().getAddress());
        shipping_address.put("city", newOrder.getShipping().getCity());
//        shipping_address.put("phone", "0928282828");
//        shipping_address.put("country_code", "IDN");

        Map<String, Object> custDetail = new HashMap<>();

//        custDetail.put("first_name", newOrder.getUser().getName());
//        custDetail.put("username", newOrder.getUser().getUsername());
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

    public static Map<String, Object> midtransMinimumRequest(Order newOrder){
        Map<String, Object> midtransRequest = new HashMap<>();
        Map<String, Object> transDetail = new HashMap<>();
        transDetail.put("order_id", newOrder.getOrderId());
        transDetail.put("gross_amount", newOrder.getItemsPrice());
        midtransRequest.put("transaction_details", transDetail);
        return midtransRequest;
    }
}
