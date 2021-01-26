package com.pamungkasaji.beshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class UserServiceException extends HttpStatusCodeException {

    private static final long serialVersionUID = 8108073890658955601L;

    public UserServiceException(HttpStatus statusCode) {
        super(statusCode);
    }

    public UserServiceException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }
}
