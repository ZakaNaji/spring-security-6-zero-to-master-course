package com.eazybytes.springsecsection1.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.addHeader("Custom-Error", "Auth Failed bc of something");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        String message = (authException != null && authException.getMessage() != null)? authException.getMessage() : "UNAUTHORIZED";
        String path = request.getRequestURI();
        String respJson = String.format("""
                {
                    "message": "%s",
                    "path": "%s"
                }
                """, message, path);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Encoding.DEFAULT_CHARSET.displayName());
        response.getWriter().write(respJson);
    }
}
