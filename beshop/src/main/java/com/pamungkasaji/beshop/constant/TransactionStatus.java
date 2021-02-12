package com.pamungkasaji.beshop.constant;

public enum TransactionStatus {
    CAPTURE("capture"),
    SETTLEMENT("settlement"),
    PENDING("pending"),
    DENY("deny"),
    CANCEL("cancel"),
    EXPIRE("expire"),
    REFUND("refund");

    public final String status;

    TransactionStatus(String status) {
        this.status = status;
    }

//    public String getStatus() {
//        return status;
//    }

    public static TransactionStatus fromString(String status) {
        for (TransactionStatus ts : TransactionStatus.values()) {
            if (ts.status.equalsIgnoreCase(status)) {
                return ts;
            }
        }
        throw new IllegalArgumentException("No transaction status constant with text " + status + " found");
    }
}
