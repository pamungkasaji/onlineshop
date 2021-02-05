package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.entity.Shipping;
import com.pamungkasaji.beshop.repository.ShippingRepository;
import com.pamungkasaji.beshop.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    ShippingRepository shippingRepository;

    @Override
    public Shipping save(Shipping shipping) {
        return shippingRepository.save(shipping);
    }
}
