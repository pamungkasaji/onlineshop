package com.pamungkasaji.beshop.repository;

import com.pamungkasaji.beshop.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
