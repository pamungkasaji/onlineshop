package com.pamungkasaji.beshop.controller;

import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.ProductEntity;
import com.pamungkasaji.beshop.security.CurrentUser;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.OrderService;
import com.pamungkasaji.beshop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<HashMap<String, Object>> getAllOrders(@CurrentUser UserPrincipal currentUser,
//                                                                @RequestParam(value = "page", defaultValue = "0") int page,
//                                                                  @RequestParam(value = "limit", defaultValue = "15") int limit) {
//        HashMap<String, Object> orderList = orderService.getAllOrders(page, limit);
//
//        return new ResponseEntity<>(orderList, HttpStatus.OK);
//    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable String id) {

        Optional<OrderEntity> order = orderService.getOrderByOrderId(id);
//        if (order.isEmpty()) {
//            throw new OrderServiceException("Order with id (" + id + ") not found!");
//        }
        return new ResponseEntity<>(order.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderEntity> createOrder(@CurrentUser UserPrincipal currentUser,
                                                     @Valid @RequestBody OrderEntity newOrder) {

        return new ResponseEntity<>(orderService.createOrder(currentUser, newOrder), HttpStatus.CREATED);
    }

}
