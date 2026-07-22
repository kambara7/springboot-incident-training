package com.example.training.incident_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.training.incident_management.model.Incident;
import com.example.training.incident_management.model.IncidentPriority;
import com.example.training.incident_management.model.IncidentStatus;

public interface IncidentRepository
extends JpaRepository<Incident, Long> {

Page<Incident> findByTitleContainingIgnoreCase(
    String title,
    Pageable pageable);

Page<Incident> findByAssigneeContainingIgnoreCase(
    String assignee,
    Pageable pageable);

Page<Incident> findByStatus(
        IncidentStatus status,
        Pageable pageable);

Page<Incident> findByPriority(
        IncidentPriority priority,
        Pageable pageable);

Page<Incident> findByStatusAndPriority(
        IncidentStatus status,
        IncidentPriority priority,
        Pageable pageable);

}