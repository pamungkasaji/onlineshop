package com.pamungkasaji.beshop.model.request.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDetailRequest {
    private String name;
    private String image;
    private String brand;
    private String category;
    private String description;
    private BigDecimal price;
    private int countInStock;
}
