package com.example.training.incident_management.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

        Incident saved =
                incidentRepository.save(incident);

        assertNotNull(saved.getId());
    }

}