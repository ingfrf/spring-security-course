package com.enmivida.app.security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class CsrfCookieFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // se busca el token en la header del request
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (Objects.nonNull(token.getHeaderName())) {
            // y se inyecta en la response
            response.setHeader(token.getHeaderName(), token.getToken());
        }
        filterChain.doFilter(request, response);
    }
}
