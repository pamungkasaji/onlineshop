package com.pamungkasaji.beshop.repository;

import com.pamungkasaji.beshop.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Optional<Product> findByProductId(String productId);
}
