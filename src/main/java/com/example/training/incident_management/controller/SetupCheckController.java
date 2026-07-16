package com.example.training.incident_management.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.training.incident_management.service.SetupCheckService;

@RestController
public class SetupCheckController {

    private final SetupCheckService setupCheckService;

    public SetupCheckController(SetupCheckService setupCheckService) {
        this.setupCheckService = setupCheckService;
    }

    @GetMapping("/setup-check")
    public Map<String, Object> setupCheck() {
        return setupCheckService.getStatus();
    }
}
