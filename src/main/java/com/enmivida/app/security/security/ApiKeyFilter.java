package com.enmivida.app.security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY = "myKey";
    private static final String API_KEY_HEADER = "api_key";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> apiKeyOptional = Optional.of(request.getHeader(API_KEY_HEADER));
            String apiKey = apiKeyOptional.orElseThrow(() -> new BadCredentialsException("No header api key"));
            if (!apiKey.equals(API_KEY)) {
                throw new BadCredentialsException("Invalid api key");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid api key");
        }
        filterChain.doFilter(request, response);
    }
}
