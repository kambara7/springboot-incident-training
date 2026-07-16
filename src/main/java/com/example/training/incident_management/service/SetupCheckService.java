package com.example.training.incident_management.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SetupCheckService {

    public Map<String, Object> getStatus() {
        Map<String, Object> result = new HashMap<>();

        result.put("app", "incident_management");
        result.put("time", LocalDateTime.now());

        return result;
    }
    
}
