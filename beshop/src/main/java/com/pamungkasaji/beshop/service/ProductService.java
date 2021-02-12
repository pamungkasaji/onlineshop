package com.pamungkasaji.beshop.service;

import com.pamungkasaji.beshop.entity.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    HashMap<String, Object> getAllProducts(int page, int limit);
    HashMap<String, Object> getSearchProducts(String keyword, int page, int limit);
    Optional<Product> getProductByProductId(String id);
    Product createProduct(Product newProduct);
    Product updateProduct(String id, Product updateProduct);
    void deleteProduct(String id);
//    void increaseStock(String productId, int amount);
//    void decreaseStock(String productId, int amount);
}
