package com.enmivida.app.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class AppControllers {

    @GetMapping(path = "/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok().body("welcome");
    }

    @GetMapping(path = "/about_us")
    public ResponseEntity<String> about() {
        return ResponseEntity.ok().body("about");
    }

    @GetMapping(path = "/loans")
    public ResponseEntity<String> loans() {
        return ResponseEntity.ok().body("loans");
    }

    @GetMapping(path = "/cards")
    public ResponseEntity<String> cards() {
        return ResponseEntity.ok().body("cards");
    }

    @GetMapping(path = "/accounts")
    public ResponseEntity<String> accounts() {
        return ResponseEntity.ok().body("accounts");
    }

    @GetMapping(path = "/balance")
    public ResponseEntity<String> balance() {
        return ResponseEntity.ok().body("balance");
    }
}
