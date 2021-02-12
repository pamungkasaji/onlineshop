package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "orders")
@Data
public class Order implements Serializable {

    public static final long serialVersionUID = 3878269912232724522L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String orderId;

    @OneToMany(mappedBy="order", cascade=CascadeType.ALL)
    private List<OrderItem> orderItems;

    private String userId;

//    @OneToOne(cascade=CascadeType.ALL)
//    @JoinColumn(name="user")
//    private UserEntity user;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="shipping")
    private Shipping shipping;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="payment")
    private Payment payment;

    @NotNull
    private BigDecimal itemsPrice;

    private String token;
    private String redirect_url;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
