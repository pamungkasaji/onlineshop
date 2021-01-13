package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "ordersItem")
public class OrderItemEntity {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

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
