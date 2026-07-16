package com.example.training.incident_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.training.incident_management.model.Incident;
import com.example.training.incident_management.service.IncidentService;

@RestController
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(
            IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("/api/incidents")
    public List<Incident> findAll() {
        return incidentService.findAll();
    }

    @GetMapping("/api/incidents/{id}")
    public Incident findById(
            @PathVariable Long id) {

        return incidentService.findById(id);
    }
}