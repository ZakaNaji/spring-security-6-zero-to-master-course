package com.eazybytes.springsecsection1.repository;

import com.eazybytes.springsecsection1.model.Authority;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    @Query(""" 
        select new org.springframework.security.core.authority.SimpleGrantedAuthority(a.name)
        from Authority a
        where a.customer.email = :email""")
    Set<GrantedAuthority> findAuthoritiesByCustomerEmail(@Param("email") String email);
}
