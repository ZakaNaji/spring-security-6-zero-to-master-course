package com.eazybytes.springsecsection1.controller;

import com.eazybytes.springsecsection1.model.Customer;
import com.eazybytes.springsecsection1.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerNewCustomer(@RequestBody Customer customer) {
        try {
            Optional<Customer> existingCustomer = customerRepository.findByEmail(customer.getEmail());
            if (existingCustomer.isPresent()) {
                throw new RuntimeException("customer with username, exist already: " + customer.getEmail());
            }
            String encode = passwordEncoder.encode(customer.getPassword());
            customer.setPassword(encode);
            Customer savedCustomer = customerRepository.save(customer);
            if (savedCustomer.getId() > 0) return ResponseEntity.status(HttpStatus.CREATED).body("new customer created with username: "+ customer.getEmail());
            throw new RuntimeException("something went wrong and couldn't register user");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error: " + e.getMessage());
        }
    }
}
