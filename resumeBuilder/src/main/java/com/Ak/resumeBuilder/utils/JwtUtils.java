package com.Ak.resumeBuilder.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(String userId) throws UnsupportedEncodingException {
        Date now = new Date();
        Date expiryDate= new Date(now.getTime()+ jwtExpiration);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigninKey())
                .compact();

    }
    private Key getSigninKey() throws UnsupportedEncodingException {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String getUserIdFromToken(String token) throws UnsupportedEncodingException {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSigninKey())
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isTokenExpired(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSigninKey())
                    .build()
                    .parseSignedClaims(token);

            return false;
        } catch (ExpiredJwtException | UnsupportedEncodingException e) {
            return true;
        }
    }
}
