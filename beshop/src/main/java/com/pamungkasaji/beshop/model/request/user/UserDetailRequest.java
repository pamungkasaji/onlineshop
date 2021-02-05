package com.pamungkasaji.beshop.model.request.user;

import lombok.Data;

@Data
public class UserDetailRequest {
    private String name;
    private String username;
    private String password;
}
