package com.eazybytes.springsecsection1.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

public final class JwtUtils {

    public static String generateJwtToken(String secret, Authentication authentication) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .issuer("znaji")
                .subject("jwt token")
                .claim("username", authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority
                ).collect(Collectors.joining(",")))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(30L, ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .compact();
    }
}
