package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "shippings")
@Data
public class Shipping {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private BigDecimal shippingPrice;

    @NotNull
    private String estimated;

    @NotNull
    private String phone;

    @NotNull
    private String address;

    @NotNull
    private String city;

    @NotNull
    private String province;

    @NotNull
    private boolean delivered = false;
}
