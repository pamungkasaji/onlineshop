package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "shippings")
@Data
public class Shipping {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private BigDecimal shippingPrice;

    @Column(nullable=false)
    private String estimated;

    @Column(nullable=false)
    private String phone;

    @Column(nullable=false)
    private String address;

    @Column(nullable=false)
    private String city;

    @Column(nullable=false)
    private String province;

    @Column(nullable=false)
    private boolean delivered = false;
}
