package com.pamungkasaji.beshop.model.request.auth;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
