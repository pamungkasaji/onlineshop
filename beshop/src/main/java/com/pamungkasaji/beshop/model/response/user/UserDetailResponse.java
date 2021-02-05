package com.pamungkasaji.beshop.model.response.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDetailResponse {

    private String userId;

    private String name;

    private String username;

    private boolean admin;

    private LocalDateTime createdAt;
}
