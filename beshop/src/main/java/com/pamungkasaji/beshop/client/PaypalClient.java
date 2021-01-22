package com.pamungkasaji.beshop.client;

import com.pamungkasaji.beshop.security.SecurityConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaypalClient {

    @GetMapping("/api/config/paypal")
    public String getPaypalClientId(){
        return SecurityConstants.getPaypalClientId();
    }
}
