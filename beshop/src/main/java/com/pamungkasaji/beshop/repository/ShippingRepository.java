package com.pamungkasaji.beshop.repository;

import com.pamungkasaji.beshop.entity.ShippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingEntity, Long> {
}
