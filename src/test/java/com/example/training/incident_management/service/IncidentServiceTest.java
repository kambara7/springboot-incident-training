package com.example.training.incident_management.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.training.incident_management.model.Incident;

class IncidentServiceTest {

    private final IncidentService incidentService =
            new IncidentService();

    @Test
    void shouldReturnAllIncidents() {

        List<Incident> incidents =
                incidentService.findAll();

        assertNotNull(incidents);
        assertEquals(2, incidents.size());
    }

    @Test
    void shouldReturnIncidentWhenIdExists() {

        Incident incident =
                incidentService.findById(1L);

        assertNotNull(incident);
        assertEquals(1L, incident.getId());
        assertEquals(
                "ログインできない",
                incident.getTitle());
    }

    @Test
    void shouldReturnNullWhenIdDoesNotExist() {

        Incident incident =
                incidentService.findById(999L);

        assertNull(incident);
    }
    
    @Test
    void shouldReturnAllIncidents1() {

        System.out.println("=== TEST START ===");

        List<Incident> incidents =
                incidentService.findAll();

        System.out.println("size = " + incidents.size());

        assertNotNull(incidents);
        assertEquals(2, incidents.size());

        System.out.println("=== TEST END ===");
    }
}