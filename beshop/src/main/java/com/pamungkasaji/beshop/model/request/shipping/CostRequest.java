package com.pamungkasaji.beshop.model.request.shipping;

import lombok.Data;

@Data
public class CostRequest {

    private String origin;
    private String destination;
    private int weight;
    private String courier;
}
