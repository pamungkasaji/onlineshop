package com.pamungkasaji.beshop.controller;

import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.OrderEntity;
import com.pamungkasaji.beshop.entity.ProductEntity;
import com.pamungkasaji.beshop.exceptions.OrderServiceException;
import com.pamungkasaji.beshop.exceptions.ResourceNotFoundException;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderEntity>> getAllOrders(@CurrentUser UserPrincipal currentUser,
                                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                                  @RequestParam(value = "limit", defaultValue = "15") int limit) {
        List<OrderEntity> orderList = orderService.getAllOrders();

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderEntity> getOrderById(@CurrentUser UserPrincipal currentUser,
                                                    @PathVariable Long id) {

        Optional<OrderEntity> order = orderService.getOrderById(id);
        if (order.isEmpty()) {
            throw new ResourceNotFoundException("Order is not found!");
        }
        if (!order.get().getUserId().equals(currentUser.getUserId())) {
            throw new OrderServiceException(HttpStatus.FORBIDDEN, "Order is not yours!");
        }

        return new ResponseEntity<>(order.get(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/myorders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderEntity>> getMyOrder(@CurrentUser UserPrincipal currentUser) {

        List<OrderEntity> order = orderService.getMyOrders(currentUser);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderEntity> createOrder(@CurrentUser UserPrincipal currentUser,
                                                     @Valid @RequestBody OrderEntity newOrder) {

        return new ResponseEntity<>(orderService.createOrder(currentUser, newOrder), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}/pay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderEntity> updatePaid(@PathVariable Long id) {

        return new ResponseEntity<>(orderService.updatePaid(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}/deliver", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderEntity> updateDelivered(@PathVariable Long id) {

        return new ResponseEntity<>(orderService.updateDelivered(id), HttpStatus.OK);
    }
}