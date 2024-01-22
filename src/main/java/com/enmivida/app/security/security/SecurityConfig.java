package com.enmivida.app.security.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    // Antes de la version 6 era necesario extender de WebSecurityAdapter

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        //auth.anyRequest().authenticated()
                        //auth.anyRequest().permitAll()
                        auth.requestMatchers("/loans", "/balance","/accounts", "/cards").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
        ;
        return http.build();
    }

    @Bean
    public UserDetailsService  userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


 /*   @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {

        UserDetails admin = User
                .withUsername("admin")
                .password("to_be_encoded")
                .authorities("ADMIN")
                .build();

        UserDetails user = User
                .withUsername("user")
                .password("to_be_encoded")
                // inserta un array de string
                .authorities("USER")
                .build();
        // inserta un array de userdetails
        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }*/
}
