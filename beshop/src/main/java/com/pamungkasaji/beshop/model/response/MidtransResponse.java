package com.pamungkasaji.beshop.model.response;

import lombok.Data;

@Data
public class MidtransResponse {
    public String paymentCode;
    public String store;
    public String transactionTime;
    public String grossAmount;
    public String currency;
    public String orderId;
    public String paymentType;
    public String signatureKey;
    public String statusCode;
    public String transactionId;
    public String transactionStatus;
    public String statusMessage;
    public String merchantId;
}
