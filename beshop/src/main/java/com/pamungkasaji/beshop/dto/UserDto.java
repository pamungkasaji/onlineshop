package com.pamungkasaji.beshop.dto;

import com.pamungkasaji.beshop.entity.Order;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 6025192569946766494L;

    private String userId;
    private Collection<String> roles;
    private String name;
    private String username;
    private String password;
    private boolean admin;
    private String encryptedPassword;
    private String token;
    private LocalDateTime createdAt;
}
