package com.pamungkasaji.beshop.model.request.midtrans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TransDetail {
    private String transactionId;
    private String transactionStatus;
    private String fraudStatus;
    private String paymentType;
    private String transactionTime;

    public TransDetail(String transactionId, String transactionStatus, String fraudStatus, String transactionTime) {
        this.transactionId = transactionId;
        this.transactionStatus = transactionStatus;
        this.fraudStatus = fraudStatus;
        this.transactionTime = transactionTime;
    }
}
