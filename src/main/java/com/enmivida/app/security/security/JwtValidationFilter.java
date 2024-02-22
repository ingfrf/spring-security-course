package com.enmivida.app.security.security;

import com.enmivida.app.security.service.JwtService;
import com.enmivida.app.security.service.JwtUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtUserDetailService jwtUserDetailService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    // Bearer con espacio al final
    private static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);
        String username = null;
        String jwt = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith(AUTHORIZATION_HEADER_BEARER)) {
            jwt = requestTokenHeader.substring(AUTHORIZATION_HEADER_BEARER.length());
            try {
                username = jwtService.getUsernameFromToken(jwt);
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage());
            } catch (ExpiredJwtException e) {
                log.warn(e.getMessage());
            }
        }

        if (username != null
                // no debe estar autenticado, por tanto, no puede existir el contexto de la autenticación
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailService.loadUserByUsername(username);
            if (this.jwtService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernameAndPasswordAuthToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernameAndPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernameAndPasswordAuthToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
