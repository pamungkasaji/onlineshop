package com.pamungkasaji.beshop.controller;


import com.pamungkasaji.beshop.model.request.shipping.CostRequest;
import com.pamungkasaji.beshop.model.request.shipping.CostResponse;
import com.pamungkasaji.beshop.model.request.shipping.RajaOngkir;
import com.pamungkasaji.beshop.model.request.shipping.RajaOngkirResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    public static final String RO_URL = "https://api.rajaongkir.com/starter";
    public static final String RO_URL_PROVINCE = "https://api.rajaongkir.com/starter/province";
    public static final String RO_URL_CITY = "https://api.rajaongkir.com/starter/city";
    public static final String RO_URL_COST = "https://api.rajaongkir.com/starter/cost";
    public static final String RO_KEY = "860498066e34689e78566d2393bb50c5";

    @GetMapping(value = "/provincelist", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProvinceList() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("key", RO_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(RO_URL_PROVINCE, HttpMethod.GET, entity, String.class).getBody();
    }

    @GetMapping(value = "/citylist", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCityList() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("key", RO_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(RO_URL_CITY, HttpMethod.GET, entity, String.class).getBody();
    }

    @GetMapping(value = "/city", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCityInProvList(@RequestParam(value = "province") int province) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("key", RO_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(RO_URL_CITY + "?province=" + province, HttpMethod.GET, entity, String.class).getBody();
    }

    @PostMapping(value = "/cost", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CostResponse costCalculation(@RequestBody CostRequest costRequest) {
        RestTemplate restTemplate = new RestTemplate();

        // default courier and origin
        costRequest.setCourier("jne");
        costRequest.setOrigin("154");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("key", RO_KEY);
        HttpEntity<CostRequest> entity = new HttpEntity<>(costRequest,headers);

        RajaOngkirResponse rajaOngkirResponse = restTemplate.postForObject(RO_URL_COST, entity, RajaOngkirResponse.class);

        assert rajaOngkirResponse != null;
        List<RajaOngkir.Result> results = rajaOngkirResponse.getRajaongkir().getResults();

        // calculate minimum shipping cost
        int min = Integer.MAX_VALUE;
        RajaOngkir.Result.Cost.Cost_ minShipping = new RajaOngkir.Result.Cost.Cost_();
        for (int i=0;i<results.size();i++){
            for (int j=0;j<results.get(i).getCosts().size();j++){
                for (int k=0;k<results.get(i).getCosts().get(j).getCost().size();k++){
                    if (results.get(i).getCosts().get(j).getCost().get(k).getValue() < min){
//                        min = results.get(i).getCosts().get(j).getCost().get(k).getValue();
                        minShipping = results.get(i).getCosts().get(j).getCost().get(k);
                    }
                }
            }
        }

        return new CostResponse(minShipping);
    }

    @PostMapping(value = "/costlist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String hippingCostList(@RequestBody CostRequest costRequest) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("key", RO_KEY);
        HttpEntity<CostRequest> entity = new HttpEntity<>(costRequest,headers);

        return restTemplate.exchange(RO_URL_COST, HttpMethod.POST, entity, String.class).getBody();
    }
}