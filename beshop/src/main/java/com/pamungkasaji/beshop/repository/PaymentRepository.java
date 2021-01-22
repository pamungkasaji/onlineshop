package com.pamungkasaji.beshop.repository;

import com.pamungkasaji.beshop.entity.PaymentResultEntity;
import com.pamungkasaji.beshop.entity.ShippingAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentResultEntity, Long> {
}
