package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "orders")
@Data
public class OrderEntity implements Serializable {

    public static final long serialVersionUID = 3878269912232724522L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy="order", cascade=CascadeType.ALL)
    private List<OrderItemEntity> orderItems;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="shipping")
    private ShippingAddressEntity shippingAddress;

    @Column(nullable=false)
    private String userId;

    @Column(nullable=false)
    private boolean isPaid = false;

    @Column(nullable=false)
    private boolean isDelivered = false;

    @Column(nullable=false)
    private BigDecimal shippingPrice;

    @Column(nullable=false)
    private BigDecimal totalPrice;

    @Column(nullable=false)
    private String paymentMethod;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
}
