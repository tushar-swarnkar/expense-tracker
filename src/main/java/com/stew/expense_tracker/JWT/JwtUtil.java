package com.stew.expense_tracker.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private String SECRET_KEY = "aR6$k!9vT@2yQp7xFzB1oK8cD#3eGhLm";

    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public String createToken(Map<String, Object> claims, String subject) { // subject == username
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("type", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60)) // 1 hour
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && (!isTokenExpired(token));
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

}
