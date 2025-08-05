package com.eazybytes.springsecsection1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvalidSessionController {

    @GetMapping("/invalidSession")
    public ResponseEntity<String> invalidSessionMessage() {
        return ResponseEntity.ok("Your session expired, try re-login.");
    }
}
