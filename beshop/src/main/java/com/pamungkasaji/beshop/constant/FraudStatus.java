package com.pamungkasaji.beshop.constant;

public enum FraudStatus {
    ACCEPT("accept"),
    DENY("deny"),
    CHALLENGE("challenge");

    public final String status;

    FraudStatus(String status) {
        this.status = status;
    }

//    public String getStatus() {
//        return status;
//    }

    public static FraudStatus fromString(String status) {
        for (FraudStatus fs : FraudStatus.values()) {
            if (fs.status.equalsIgnoreCase(status)) {
                return fs;
            }
        }
        throw new IllegalArgumentException("No transaction status constant with text " + status + " found");
    }
}
