package com.eazybytes.springsecsection1.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.addHeader("X-Denied-X", "You don't have access rights");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        String message = (accessDeniedException != null && accessDeniedException.getMessage() != null)? accessDeniedException.getMessage() : "UNAUTHORIZED";
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
