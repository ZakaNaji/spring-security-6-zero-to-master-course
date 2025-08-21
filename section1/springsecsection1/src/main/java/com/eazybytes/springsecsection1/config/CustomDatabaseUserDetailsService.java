package com.eazybytes.springsecsection1.config;

import com.eazybytes.springsecsection1.model.Customer;
import com.eazybytes.springsecsection1.repository.AuthorityRepository;
import com.eazybytes.springsecsection1.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service("userDetailsService")
public class CustomDatabaseUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final AuthorityRepository authorityRepository;

    public CustomDatabaseUserDetailsService(CustomerRepository customerRepository, AuthorityRepository authorityRepository) {
        this.customerRepository = customerRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found: " + username));
        Collection<? extends GrantedAuthority> authorities = authorityRepository.findAuthoritiesByCustomerEmail(customer.getEmail());
        return new User(username, customer.getPassword(), authorities);
    }
}
