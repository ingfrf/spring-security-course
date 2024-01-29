package com.enmivida.app.security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    // Antes de la version 6 era necesario extender de WebSecurityAdapter

    private final CsrfCookieFilter csrfCookieFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // The CsrfTokenRequestAttributeHandler makes the CsrfToken available as an HttpServletRequest attribute called _csrf.
        // This implementation also resolves the token value from the request as either a request header
        // (one of X-CSRF-TOKEN or X-XSRF-TOKEN by default) or a request parameter (_csrf by default).
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http
                .authorizeHttpRequests(auth ->
                        //auth.anyRequest().authenticated()
                        //auth.anyRequest().permitAll()
                        auth.requestMatchers("/loans", "/balance", "/accounts", "/cards").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
        ;
        http.cors(cors -> corsConfigurationSource());
        http.csrf(csrf -> csrf
                        // se añade el handler ya definido
                        .csrfTokenRequestHandler(requestHandler)
                        // se excluyen las rutas 'abiertas' que no necesitan autenticacion
                        .ignoringRequestMatchers("/welcome", "/about_us")
                        // The CookieCsrfTokenRepository writes to a cookie named XSRF-TOKEN
                        // and reads it from an HTTP request header named X-XSRF-TOKEN or the request parameter _csrf by default
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                // se añade el filtro definido
                .addFilterAfter(csrfCookieFilter, BasicAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // se puede hacer indicando los dominios exactos
        // config.setAllowedOrigins(List.of("http://localhost:4200", "http://my-app.com"));
        // permite cualquier origen
        config.setAllowedOrigins(List.of("*"));

        // se pueden habilitar solo algunos metodos
        // config.setAllowedMethods(List.of("GET", "POST"));
        // o a todos
        config.setAllowedMethods(List.of("*"));
        // se admiten todas las cabeceras
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // se aplica a todos las rutas
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
