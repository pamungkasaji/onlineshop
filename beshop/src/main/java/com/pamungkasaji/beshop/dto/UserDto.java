package com.pamungkasaji.beshop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 6025192569946766494L;

    private long id;
    private String userId;
    private String name;
    private String email;
    private String password;
    private boolean admin;
    private String token;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private Collection<String> roles;
}
