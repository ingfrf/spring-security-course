package com.enmivida.app.security.service;

import com.enmivida.app.security.entity.RoleEntity;
import com.enmivida.app.security.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerUserDetails implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByEmail(username)
                .map(ce -> User
                        .withUsername(ce.getEmail())
                        .password(ce.getPassword())
                        // esto recibe una lista
                        .authorities(authorities(ce.getRoles()))
                        .build()
                ).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private List<SimpleGrantedAuthority> authorities(List<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}

