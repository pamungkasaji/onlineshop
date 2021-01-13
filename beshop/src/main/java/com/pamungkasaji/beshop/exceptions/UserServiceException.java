package com.pamungkasaji.beshop.exceptions;

public class UserServiceException extends RuntimeException{

    private static final long serialVersionUID = 8108073890658955601L;

    public UserServiceException(String message) {
        super(message);
    }
}
