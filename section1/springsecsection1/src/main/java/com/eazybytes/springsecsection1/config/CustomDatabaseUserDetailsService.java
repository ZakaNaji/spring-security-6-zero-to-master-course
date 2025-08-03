package com.eazybytes.springsecsection1.config;

import com.eazybytes.springsecsection1.model.Customer;
import com.eazybytes.springsecsection1.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomDatabaseUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomDatabaseUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found: " + username));
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRole().name()));
        return new User(username, customer.getPassword(), authorities);
    }
}
