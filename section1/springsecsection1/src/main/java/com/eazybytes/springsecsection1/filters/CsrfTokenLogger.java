package com.eazybytes.springsecsection1.filters;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;

import java.io.IOException;

@Slf4j
public class CsrfTokenLogger implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        CsrfToken csrfToken = (CsrfToken) servletRequest.getAttribute("_csrf");

        log.warn("CSRF TOKEN: " + csrfToken.getToken());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
