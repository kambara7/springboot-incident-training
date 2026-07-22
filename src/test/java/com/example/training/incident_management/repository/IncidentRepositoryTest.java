package com.example.training.incident_management.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.training.incident_management.model.Incident;
import com.example.training.incident_management.model.IncidentPriority;
import com.example.training.incident_management.model.IncidentStatus;

@DataJpaTest
class IncidentRepositoryTest {

    @Autowired
    private IncidentRepository incidentRepository;

    @Test
    void shouldSaveIncident() {

        Incident incident = new Incident();

        incident.setTitle("Repositoryテスト");
        incident.setDescription("保存確認");
        incident.setStatus(IncidentStatus.OPEN);
        incident.setPriority(IncidentPriority.HIGH);
        incident.setAssignee("神原");
        incident.setOccurredAt(LocalDateTime.now());

        Incident saved = incidentRepository.save(incident);

        assertNotNull(saved.getId());
        assertEquals("Repositoryテスト", saved.getTitle());
        assertEquals(IncidentStatus.OPEN, saved.getStatus());
        assertEquals(IncidentPriority.HIGH, saved.getPriority());
    }
    
    @Test
    void shouldFindIncidentByTitle() {

        Incident incident = new Incident();

        incident.setTitle("Login Error");
        incident.setDescription("Search test");
        incident.setStatus(IncidentStatus.OPEN);
        incident.setPriority(IncidentPriority.HIGH);
        incident.setAssignee("Tanaka");
        incident.setOccurredAt(LocalDateTime.now());

        incidentRepository.save(incident);

        Page<Incident> result =
                incidentRepository
                        .findByTitleContainingIgnoreCase(
                                "login",
                                PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(
                "Login Error",
                result.getContent().get(0).getTitle());
    }
    
    @Test
    void shouldFindIncidentsWithPaging() {

        for (int i = 1; i <= 3; i++) {

            Incident incident = new Incident();

            incident.setTitle("Incident " + i);
            incident.setDescription("Paging test");
            incident.setStatus(IncidentStatus.OPEN);
            incident.setPriority(IncidentPriority.HIGH);
            incident.setAssignee("Tanaka");
            incident.setOccurredAt(LocalDateTime.now());

            incidentRepository.save(incident);
        }

        Page<Incident> result =
                incidentRepository.findAll(
                        PageRequest.of(0, 2));

        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
    }
}