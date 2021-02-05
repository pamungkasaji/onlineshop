package com.pamungkasaji.beshop.model.response.auth;

import lombok.Data;

@Data
public class SignUpResponse {

    private String userId;

    private String name;

    private String username;

    private boolean isAdmin;

    private String token;
}
