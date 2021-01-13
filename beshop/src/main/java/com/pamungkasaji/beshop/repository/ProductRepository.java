package com.pamungkasaji.beshop.repository;

import com.pamungkasaji.beshop.entity.ProductEntity;
import com.pamungkasaji.beshop.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByProductId(String productId);
}
