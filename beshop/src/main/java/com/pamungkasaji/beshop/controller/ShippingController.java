package com.pamungkasaji.beshop.controller;

import com.pamungkasaji.beshop.model.request.shipping.CostRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    public static final String RO_URL = "https://api.rajaongkir.com/starter";
    public static final String RO_URL_PROVINCE = "https://api.rajaongkir.com/starter/province";
    public static final String RO_URL_CITY = "https://api.rajaongkir.com/starter/city";
    public static final String RO_URL_COST = "https://api.rajaongkir.com/starter/cost";
    public static final String RO_KEY = "your api key";

    @GetMapping(value = "/province", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProvinceList() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("key", RO_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(RO_URL_PROVINCE, HttpMethod.GET, entity, String.class).getBody();
    }

    @GetMapping(value = "/city", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCityList() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("key", RO_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(RO_URL_CITY, HttpMethod.GET, entity, String.class).getBody();
    }

    @PostMapping(value = "/cost", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createProducts(@RequestBody CostRequest costRequest) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("key", RO_KEY);
        HttpEntity<CostRequest> entity = new HttpEntity<>(costRequest,headers);

        return restTemplate.exchange(RO_URL_COST, HttpMethod.POST, entity, String.class).getBody();
    }
}
