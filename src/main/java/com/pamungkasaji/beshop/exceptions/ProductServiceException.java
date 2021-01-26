package com.pamungkasaji.beshop.exceptions;

public class ProductServiceException extends RuntimeException{

    private static final long serialVersionUID = -3461832747485338696L;

    public ProductServiceException(String message) {
        super(message);
    }
}
