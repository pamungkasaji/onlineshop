package com.pamungkasaji.beshop.controller;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import com.midtrans.service.MidtransSnapApi;
import com.pamungkasaji.beshop.constant.FraudStatus;
import com.pamungkasaji.beshop.constant.TransactionStatus;
import com.pamungkasaji.beshop.entity.Order;
import com.pamungkasaji.beshop.entity.Payment;
import com.pamungkasaji.beshop.exceptions.OrderServiceException;
import com.pamungkasaji.beshop.model.request.midtrans.NotifRequest;
import com.pamungkasaji.beshop.model.request.midtrans.TransDetail;
import com.pamungkasaji.beshop.security.CurrentUser;
import com.pamungkasaji.beshop.security.SecurityConstants;
import com.pamungkasaji.beshop.security.UserPrincipal;
import com.pamungkasaji.beshop.service.OrderService;
import com.pamungkasaji.beshop.service.PaymentService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {

//    Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    OrderService orderService;

    PaymentService paymentService;

    public PaymentController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
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

    private MidtransCoreApi coreApi = new ConfigFactory(
            new Config("SB-Mid-server-DIlYFfq4uoDp90490aR62jkJ",
                    "SB-Mid-client-1AvIQh0PMn9imV91",
                    false))
            .getCoreApi();

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    @PutMapping(value = "/api/orders/{id}/pay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Order> updatePay(@PathVariable String id,
//                                           @CurrentUser UserPrincipal currentUser,
//                                           @RequestBody Payment payment) {
//
//        Order order = orderService.getOrderById(id);
//        if (!currentUser.isAdmin() && !order.getUserId().equals(currentUser.getUserId())) {
//            throw new OrderServiceException(HttpStatus.FORBIDDEN, "Order is not yours!");
//        }
//
//        return new ResponseEntity<>(paymentService.updatePaypal(id, currentUser, payment), HttpStatus.OK);
//    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/api/orders/{id}/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkTransaction(@PathVariable String id) throws MidtransError {
        JSONObject result = coreApi.checkTransaction(id);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    // Midtrans Handling Notification
    @PostMapping(value = "/api/payment/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> response) throws MidtransError {
        String notifResponse = null;
//        Order orderReturn = null;
        if (!(response.isEmpty())) {
            //Get Order ID from notification body
            String orderId = (String) response.get("order_id");

            // Get status transaction to api with order id
            JSONObject transactionResult = coreApi.checkTransaction(orderId);

            TransDetail transDetail = new TransDetail(
                    transactionResult.get("transaction_id").toString(),
                    transactionResult.get("transaction_status").toString(),
                    transactionResult.get("fraud_status").toString(),
                    transactionResult.get("transaction_time").toString()
            );

            Order orderReturn = paymentService.setPaymentStatus(orderId, transDetail);

            notifResponse = "Transaction notification received. Order ID: " + orderReturn.getOrderId() + ". Transaction status: "
                    + orderReturn.getPayment().getTransactionStatus() + ". Fraud status: " + orderReturn.getPayment().getFraudStatus();


//            String transactionStatus = (String) transactionResult.get("transaction_status");
//            String fraudStatus = (String) transactionResult.get("fraud_status");
//            String transactionTime = (String) transactionResult.get("transaction_time");
//
//            notifResponse = "Transaction notification received. Order ID: " + orderId + ". Transaction status: " + transactionStatus + ". Fraud status: " + fraudStatus;
//            logger.info(notifResponse);
//
//            if (transactionStatus.equals("capture")) {
//                if (fraudStatus.equals("challenge")) {
//                    logger.info("fraudStatus challenge");
//                    paymentService.unsuccessPayment(orderId, TransactionStatus.CAPTURE, FraudStatus.CHALLENGE);
//                } else if (fraudStatus.equals("accept")) {
//                    logger.info("fraudStatus accept");
//                    paymentService.successPayment(orderId, payment);
//                }
//            } else if (transactionStatus.equals("settlement")) {
//                logger.info("transactionStatus settlement");
//                paymentService.setPaymentStatus(orderId, payment);
//            } else if (transactionStatus.equals("cancel") || transactionStatus.equals("deny") || transactionStatus.equals("expire")) {
//                logger.info("(transactionStatus cancel");
//                paymentService.setPaymentStatus(orderId, payment);
//            } else if (transactionStatus.equals("pending")) {
//                logger.info("transactionStatus pending");
//                paymentService.setPaymentStatus(orderId, payment);
//            }
        }
        return new ResponseEntity<>(notifResponse, HttpStatus.OK);
    }
}
