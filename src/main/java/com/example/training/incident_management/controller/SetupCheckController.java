package com.example.training.incident_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SetupCheckController {

    @GetMapping("/setup-check")
    public String setupCheck() {
        return "Setup OK";
    }
}
