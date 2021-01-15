package com.pamungkasaji.beshop.repository;

import com.pamungkasaji.beshop.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findById(Long orderId);
    List<OrderEntity> findByUserId(String userId);
}