package com.eazybytes.springsecsection1.repository;

import com.eazybytes.springsecsection1.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


  Optional<Customer> findByEmail(String email);
}