package com.pamungkasaji.beshop.model.request.midtrans;

import lombok.Data;

@Data
public class NotifRequest {
    private String transactionTime;
    private String transactionStatus;
    private String transactionId;
    private String statusMessage;
    private String statusCode;
    private String signatureKey;
    private String paymentType;
    private String orderId;
    private String merchantId;
    private String maskedCard;
    private String grossAmount;
    private String fraudStatus;
    private String eci;
    private String currency;
    private String channelResponseMessage;
    private String channelResponseCode;
    private String cardType;
    private String bank;
    private String approvalCode;
}
