package com.pamungkasaji.beshop.service;

import com.pamungkasaji.beshop.entity.ProductEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    HashMap<String, Object> getAllProducts(int page, int limit);
    Optional<ProductEntity> getProductByProductId(String id);
    ProductEntity createProduct(ProductEntity newProduct);
    ProductEntity updateProduct(String id, ProductEntity updateProduct);
    void deleteProduct(String id);
}
