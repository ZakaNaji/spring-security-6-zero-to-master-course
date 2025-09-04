package com.eazybytes.springsecsection1.config;

import com.eazybytes.springsecsection1.exceptionhandling.CustomAccessDeniedHandler;
import com.eazybytes.springsecsection1.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.eazybytes.springsecsection1.filters.CsrfTokenLogger;
import com.eazybytes.springsecsection1.filters.ExposeCsrfTokenFilter;
import com.eazybytes.springsecsection1.filters.JwtTokenGeneratorFilter;
import com.eazybytes.springsecsection1.filters.JwtTokenValidatorFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Profile("prod")
public class ProdSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.addAllowedOrigin("http://localhost:4200");
                        corsConfiguration.addAllowedMethod("*");
                        corsConfiguration.addAllowedHeader("*");
                        corsConfiguration.addExposedHeader("Authorization");
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setMaxAge(3600L);
                        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
                        return corsConfiguration;
                    }
                }))
                .csrf(csrfConfig -> csrfConfig
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers("/contact", "/register"))
                .addFilterAfter(new ExposeCsrfTokenFilter(), BasicAuthenticationFilter.class)
                //.addFilterAfter(new CsrfTokenLogger(), CsrfFilter.class)
                .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/welcome","/contact", "/notices", "/error", "/register", "/invalidSession").permitAll()
                        //.requestMatchers("/myBalance").hasAnyAuthority("VIEWBALANCE")
                        //.requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                        //.requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                        //.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                        .requestMatchers("/myBalance").hasRole("USER")
                        .requestMatchers("/myLoans").hasRole("USER")
                        .requestMatchers("/myCards").hasRole("ADMIN")
                        .requestMatchers("/myAccount").hasAnyRole("USER", "ADMIN")
                )
                .httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()))
                .exceptionHandling(exp -> exp
                        .authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())// global exception handling via the entry point
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
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

    @Bean
    ApplicationListener<AuthenticationSuccessEvent> authenticationSuccessEventApplicationListener() {
        return e -> {
            Authentication authentication = e.getAuthentication();
            System.out.println("[%s] was successfully auth using [%s]".formatted(authentication.getName(), authentication.getClass().getSimpleName()));
        };
    }
}
