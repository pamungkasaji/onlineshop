package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity(name = "payment")
public class PaymentEntity {

    @JsonIgnore
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable=false)
    private String paymentId;

    @Column(nullable=false)
    private boolean paid = false;

    @Column(nullable=false)
    private String paymentMethod;

    @Column(nullable=false)
    private String status;

    private String updateTime;

    public PaymentEntity(String paymentId, String paymentMethod, String status, String updateTime) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.updateTime = updateTime;
    }

    //    @Column(nullable=false)
//    private String emailAddress;
}
