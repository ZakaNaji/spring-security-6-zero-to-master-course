package com.eazybytes.springsecsection1.controller;


import com.eazybytes.springsecsection1.dto.LoginRequest;
import com.eazybytes.springsecsection1.dto.LoginResponse;
import com.eazybytes.springsecsection1.model.Customer;
import com.eazybytes.springsecsection1.repository.CustomerRepository;
import com.eazybytes.springsecsection1.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Value("${jwt.token.secret}")
    private String jwtTokenSecret;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String hashPwd = passwordEncoder.encode(customer.getPassword());
            customer.setPassword(hashPwd);
            customer.setCreateDt(new Date(System.currentTimeMillis()));
            Customer savedCustomer = customerRepository.save(customer);

            if (savedCustomer.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("Given user details are successfully registered");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("User registration failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An exception occurred: " + ex.getMessage());
        }
    }

    @RequestMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(authentication.getName());
        return optionalCustomer.orElse(null);
    }

    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponse> apiAuth(@RequestBody LoginRequest request) {
        String jwt = "";
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password());
        Authentication authResult = authenticationManager.authenticate(authentication);
        if (authResult != null && authResult.isAuthenticated()) {
            jwt = JwtUtils.generateJwtToken(jwtTokenSecret, authResult);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Authorization", jwt)
                .body(new LoginResponse(HttpStatus.CREATED.getReasonPhrase(), jwt));
    }

}
