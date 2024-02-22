package com.enmivida.app.security.controller;

import com.enmivida.app.security.entity.JwtRequest;
import com.enmivida.app.security.entity.JwtResponse;
import com.enmivida.app.security.service.JwtService;
import com.enmivida.app.security.service.JwtUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailService jwtUserDetailService;
    private final JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> postToken(@RequestBody JwtRequest request) {
        this.authenticate(request);
        UserDetails userDetails = this.jwtUserDetailService.loadUserByUsername(request.getUsername());
        String token = this.jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(JwtRequest request) {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException | DisabledException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
