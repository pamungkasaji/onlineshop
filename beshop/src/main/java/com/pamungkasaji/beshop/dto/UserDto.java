package com.pamungkasaji.beshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pamungkasaji.beshop.entity.OrderEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 6025192569946766494L;

    private long id;
    private String userId;
    private List<OrderEntity> order;
    private String name;
    private String email;
    private String password;
    private boolean admin;
    private String token;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private Collection<String> roles;
    private LocalDateTime createdAt;
}
