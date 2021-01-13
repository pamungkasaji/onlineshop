package com.pamungkasaji.beshop.repository;

import com.pamungkasaji.beshop.entity.ShippingAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddressEntity, Long> {
}
