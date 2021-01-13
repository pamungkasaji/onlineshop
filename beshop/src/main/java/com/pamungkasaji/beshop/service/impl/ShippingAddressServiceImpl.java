package com.pamungkasaji.beshop.service.impl;

import com.pamungkasaji.beshop.entity.ShippingAddressEntity;
import com.pamungkasaji.beshop.repository.ShippingAddressRepository;
import com.pamungkasaji.beshop.service.ShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {

    @Autowired
    ShippingAddressRepository shippingAddressRepository;

    @Override
    public ShippingAddressEntity save(ShippingAddressEntity shippingAddress) {
        return shippingAddressRepository.save(shippingAddress);
    }
}
