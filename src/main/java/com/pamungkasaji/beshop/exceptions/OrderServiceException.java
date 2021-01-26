package com.pamungkasaji.beshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class OrderServiceException extends HttpStatusCodeException {

    public OrderServiceException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public OrderServiceException(HttpStatus message) {
        super(message);
    }

    public OrderServiceException(HttpStatus statusCode, String message) {
        super(statusCode, message);
    }
}
