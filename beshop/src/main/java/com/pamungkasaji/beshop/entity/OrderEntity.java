package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "orders")
@Data
public class OrderEntity implements Serializable {

    public static final long serialVersionUID = 3878269912232724522L;

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "order")
    private Set<OrderItemEntity> orderItems = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "order")
    private ShippingAddressEntity shippingAddress;

    //    @OneToOne(fetch = FetchType.EAGER)
//    private ShippingAddressEntity shippingAddress;

    @Column(nullable=false)
    private String userid;

    @Column(nullable=false)
    private BigDecimal shippingPrice;

    @Column(nullable=false)
    private BigDecimal totalPrice;

    @Column(nullable=false)
    private String paymentMethod;
}
