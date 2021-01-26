package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "paymentResult")
public class PaymentEntity {

    @JsonIgnore
    @Id
    @GeneratedValue
    private long paymentId;

    @Column(nullable=false)
    private String id;

    @Column(nullable=false)
    private String status;

    private String updateTime;

    public PaymentEntity(String id, String status, String updateTime) {
        this.id = id;
        this.status = status;
        this.updateTime = updateTime;
    }

    //    @Column(nullable=false)
//    private String emailAddress;
}
