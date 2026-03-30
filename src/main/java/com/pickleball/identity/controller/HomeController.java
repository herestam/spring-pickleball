package com.pickleball.identity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "message", "Welcome to Pickleball API",
                "status", "OK",
                "version", "v1",
                "timestamp", System.currentTimeMillis()
        );
    }
}
