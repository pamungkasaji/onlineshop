package com.pamungkasaji.beshop.controller;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import com.midtrans.service.MidtransSnapApi;
import com.pamungkasaji.beshop.entity.PaymentEntity;
import com.pamungkasaji.beshop.security.SecurityConstants;
import com.pamungkasaji.beshop.service.OrderService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PaymentController {

    OrderService orderService;

    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }

    public static String MIDTRANSCLIENTKEY;
    public static String MIDTRANSSERVERKEY;

    @Value("${midtransClientKey}")
    public void setMidtransClientKey(String key) {
        MIDTRANSCLIENTKEY = key;
    }

    @Value("${midtransClientKey}")
    public void setMidtransServerKey(String key) {
        MIDTRANSSERVERKEY = key;
    }

    private MidtransSnapApi snapApi = new ConfigFactory(
            new Config(MIDTRANSSERVERKEY,
                    MIDTRANSCLIENTKEY,
                    false))
            .getSnapApi();

    private MidtransCoreApi coreApi = new ConfigFactory(
            new Config(MIDTRANSSERVERKEY,
                    MIDTRANSCLIENTKEY,
                    false))
            .getCoreApi();

    @GetMapping("/api/config/paypal")
    public String getMidtransClientInfo(){
        return SecurityConstants.getPaypalClientId();
    }

    // COBA
    // API `/charge` for mobile SDK to get Snap Token
    @PostMapping(value = "/api/orders/checkout", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object createTokeSnap(@RequestBody Map<String, Object> body) throws MidtransError {
        JSONObject result = snapApi.createTransaction(body);
        result.put("clientKey", MIDTRANSCLIENTKEY);
        return result.toString();
    }

    // Midtrans Handling Notification
    @PostMapping(value = "/api/payment/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> response) throws MidtransError {
        String notifResponse = null;
        if (!(response.isEmpty())) {
            //Get Order ID from notification body
            String orderId = (String) response.get("order_id");

            // Get status transaction to api with order id
            JSONObject transactionResult = coreApi.checkTransaction(orderId);

            PaymentEntity payment = new PaymentEntity(
                    transactionResult.get("transaction_id").toString(),
                    transactionResult.get("transaction_status").toString(),
                    transactionResult.get("transaction_time").toString()
            );

            String transactionStatus = (String) transactionResult.get("transaction_status");
            String fraudStatus = (String) transactionResult.get("fraud_status");

            notifResponse = "Transaction notification received. Order ID: " + orderId + ". Transaction status: " + transactionStatus + ". Fraud status: " + fraudStatus;
            System.out.println(notifResponse);

            if (transactionStatus.equals("capture")) {
                if (fraudStatus.equals("challenge")) {
                    orderService.setPaymentStatus(orderId, payment);
                } else if (fraudStatus.equals("accept")) {
                    orderService.setPaymentStatus(orderId, payment);
                }
            } else if (transactionStatus.equals("cancel") || transactionStatus.equals("deny") || transactionStatus.equals("expire")) {
                orderService.setPaymentStatus(orderId, payment);
            } else if (transactionStatus.equals("pending")) {
                orderService.setPaymentStatus(orderId, payment);
            }
        }
        return new ResponseEntity<>(notifResponse, HttpStatus.OK);
    }
}