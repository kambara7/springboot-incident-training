package com.example.training.incident_management.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.training.incident_management.dto.IncidentCreateRequest;
import com.example.training.incident_management.dto.IncidentResponse;
import com.example.training.incident_management.dto.IncidentUpdateRequest;
import com.example.training.incident_management.model.Incident;
import com.example.training.incident_management.model.IncidentPriority;
import com.example.training.incident_management.model.IncidentStatus;
import com.example.training.incident_management.service.IncidentService;

import jakarta.validation.Valid;

@RestController
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(
            IncidentService incidentService) {

        this.incidentService = incidentService;
    }

    @GetMapping(
            value = "/api/incidents",
            produces = "application/json;charset=UTF-8"
    )
    public Page<Incident> findAll(

            @RequestParam(required = false)
            String title,

            @RequestParam(required = false)
            String assignee,

            @RequestParam(required = false)
            IncidentStatus status,

            @RequestParam(required = false)
            IncidentPriority priority,

            Pageable pageable) {

        return incidentService.findAll(
                title,
                assignee,
                status,
                priority,
                pageable);
    }


    @GetMapping(
            value = "/api/incidents/{id}",
            produces = "application/json;charset=UTF-8"
    )
    public Incident findById(
            @PathVariable Long id) {

        return incidentService.findById(id);
    }

    @PostMapping("/api/incidents")
    public ResponseEntity<IncidentResponse> create(
            @Valid
            @RequestBody IncidentCreateRequest request) {

        IncidentResponse response =
                incidentService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/api/incidents/{id}")
    public ResponseEntity<IncidentResponse> update(
            @PathVariable Long id,
            @RequestBody IncidentUpdateRequest request) {

        IncidentResponse response =
                incidentService.update(id, request);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/incidents/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        boolean deleted =
                incidentService.delete(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}