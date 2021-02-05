package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity(name = "payments")
public class Payment {

    @JsonIgnore
    @Id
    @GeneratedValue
    private long id;

    private String paymentId;

    @Column(nullable=false)
    private boolean paid = false;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paidAt;

    private String paymentMethod;

    private String status;

    private String updateTime;

    public Payment(String paymentId, String paymentMethod, String status, String updateTime) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.updateTime = updateTime;
    }
}
