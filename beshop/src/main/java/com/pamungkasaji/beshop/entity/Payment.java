package com.pamungkasaji.beshop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pamungkasaji.beshop.constant.FraudStatus;
import com.pamungkasaji.beshop.constant.TransactionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity(name = "payments")
public class Payment {

    @JsonIgnore
    @Id
    @GeneratedValue
    private long id;

    private String transactionId;

    @NotNull
    private boolean paid = false;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date paidAt;

    private String paymentMethod;

    private String paymentType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    private FraudStatus fraudStatus;
}
