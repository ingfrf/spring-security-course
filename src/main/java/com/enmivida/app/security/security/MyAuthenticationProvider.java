package com.enmivida.app.security.security;

import com.enmivida.app.security.entity.CustomerEntity;
import com.enmivida.app.security.entity.RoleEntity;
import com.enmivida.app.security.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

//@Component
@RequiredArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        CustomerEntity customer = repository.findByEmail(username).orElseThrow(() -> new BadCredentialsException("Invalid Credentials"));

        if (passwordEncoder.matches(pwd, customer.getPassword())) {
            List<RoleEntity> roles = customer.getRoles();
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
        }
        throw new BadCredentialsException("Invalid Credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // return false, se puede dejar así
        // aunque vamos a retornar una implementación más 'accurate'
        // la autenticación se hará mediante user/pass
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
