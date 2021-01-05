package com.pamungkasaji.beshop.model.response.auth;

import lombok.Data;

@Data
public class SignUpResponse {

    private String userId;

    private String name;

    private String email;

    private String token;
}
