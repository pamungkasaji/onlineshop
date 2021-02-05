package com.pamungkasaji.beshop.shared;

import com.pamungkasaji.beshop.security.SecurityConstants;
import com.pamungkasaji.beshop.security.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class Utils {

    public String generateToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userPrincipal.getUsername(), userPrincipal.getUserId());
    }

    private String createToken(Map<String, Object> claims, String subject, String userId) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.getTokenSecret())
                .compact();
    }
}
