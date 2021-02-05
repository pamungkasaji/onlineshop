package com.pamungkasaji.beshop.model.response.auth;

import lombok.Data;

@Data
public class LoginResponse {

    private String userId;

    private String name;

    private String username;

    private String token;

    private boolean admin;
}
