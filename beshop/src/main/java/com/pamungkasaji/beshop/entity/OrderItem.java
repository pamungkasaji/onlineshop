package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "ordersItem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="order_id")
    @JsonIgnore
    private Order order;

    @Column(nullable=false)
    private String product;

    @Column(nullable=false)
    private String name;

    private String image;

    @Column(nullable=false)
    private BigDecimal price;

    @Column(nullable=false)
    private int qty;
}
