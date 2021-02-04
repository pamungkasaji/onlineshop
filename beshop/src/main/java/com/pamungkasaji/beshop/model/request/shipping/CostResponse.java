package com.pamungkasaji.beshop.model.request.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CostResponse{
    private RajaOngkir.Result.Cost.Cost_ shippingCost;
}
