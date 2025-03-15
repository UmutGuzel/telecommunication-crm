package com.gygy.userservice.core.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import java.security.Key;
import io.jsonwebtoken.Claims;
import javax.crypto.SecretKey;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.entity.Role;
import com.gygy.userservice.entity.Permission;

@Service
public class JwtService {
    @Value("${jwt.expiration}")
    private long ExpirationTime;

    @Value("${jwt.secret}")
    private String SecretKey;

    public String generateToken(User user) {
        return Jwts.builder()
                .claims(createClaims(user))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ExpirationTime))
                .signWith(getSignKey())
                .compact();
    }

    public boolean verifyToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration().after(new Date());
    }

    public String extractEmail(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("email", String.class);
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("roles", List.class);
    }

    public List<String> extractPermissions(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("permissions", List.class);
    }

    public String extractId(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("userId", String.class);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());
        claims.put("email", user.getEmail());

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        Set<String> permissions = new HashSet<>();

        if (user.getPermissions() != null) {
            permissions.addAll(user.getPermissions().stream()
                    .map(Permission::getName)
                    .collect(Collectors.toSet()));
        }

        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> {
                if (role.getPermissions() != null) {
                    permissions.addAll(role.getPermissions().stream()
                            .map(Permission::getName)
                            .collect(Collectors.toSet()));
                }
            });
        }

        claims.put("permissions", new ArrayList<>(permissions));

        return claims;
    }

}
