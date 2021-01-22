package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "paymentResult")
public class PaymentResultEntity {

    @JsonIgnore
    @Id
    @GeneratedValue
    private long paymentId;

    @Column(nullable=false)
    private String id;

    @Column(nullable=false)
    private String status;

    private String updateTime;

//    @Column(nullable=false)
//    private String emailAddress;
}
