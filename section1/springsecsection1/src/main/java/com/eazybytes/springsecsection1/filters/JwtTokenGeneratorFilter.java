package com.eazybytes.springsecsection1.filters;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Environment environment = getEnvironment();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && environment != null) {
            String secret = environment.getProperty("JWT_SECRET_KEY", "my-super-long-secret-key-1234567890-!@#$%");
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            String jwtToken = Jwts.builder()
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

            response.addHeader("Authorization", jwtToken);

        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/user");
    }
}
