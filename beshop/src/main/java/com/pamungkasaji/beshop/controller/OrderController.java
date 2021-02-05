package com.pamungkasaji.beshop.controller;

import com.midtrans.httpclient.error.MidtransError;
import com.pamungkasaji.beshop.entity.Order;
import com.pamungkasaji.beshop.exceptions.OrderServiceException;
import com.pamungkasaji.beshop.model.response.order.OrderList;
import com.pamungkasaji.beshop.security.CurrentUser;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderList> getAllOrders(@CurrentUser UserPrincipal currentUser,
                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "limit", defaultValue = "15") int limit) {
        OrderList orderList = new OrderList(orderService.getAllOrders());

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrderById(@CurrentUser UserPrincipal currentUser,
                                              @PathVariable String id) {

        Order order = orderService.getOrderById(id);
        if (!currentUser.isAdmin() && !order.getUserId().equals(currentUser.getUserId())) {
            throw new OrderServiceException(HttpStatus.FORBIDDEN, "Order is not yours!");
        }

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/myorders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderList> getMyOrder(@CurrentUser UserPrincipal currentUser) {

        OrderList orderList = new OrderList(orderService.getMyOrders(currentUser));

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createOrder(@CurrentUser UserPrincipal currentUser,
                                             @Valid @RequestBody Order newOrder) throws MidtransError {

        return new ResponseEntity<>(orderService.createOrder(currentUser, newOrder), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}/deliver", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> updateDelivered(@PathVariable String id) {

        return new ResponseEntity<>(orderService.updateDelivered(id), HttpStatus.OK);
    }
}
