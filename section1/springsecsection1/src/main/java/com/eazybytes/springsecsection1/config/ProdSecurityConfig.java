package com.eazybytes.springsecsection1.config;

import com.eazybytes.springsecsection1.exceptionhandling.CustomAccessDeniedHandler;
import com.eazybytes.springsecsection1.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("prod")
public class ProdSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(req ->
                req.requestMatchers("/welcome","/contact", "/notices", "/error", "/register", "/invalidSession").permitAll().anyRequest().authenticated())
                .httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exp -> exp
                        .authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())// global exception handling via the entry point
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .sessionManagement(smc -> smc
                        .invalidSessionUrl("/invalidSession")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*@Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }*/
}
