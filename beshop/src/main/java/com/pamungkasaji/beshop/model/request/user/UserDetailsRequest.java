package com.pamungkasaji.beshop.model.request.user;

import lombok.Data;

@Data
public class UserDetailsRequest {

    private String name;

    private String email;

    private String password;
}
