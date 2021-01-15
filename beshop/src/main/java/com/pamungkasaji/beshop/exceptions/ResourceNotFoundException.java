package com.pamungkasaji.beshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ResourceNotFoundException extends HttpStatusCodeException {

    private static final long serialVersionUID = 1783122521840822341L;

    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
