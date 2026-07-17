package com.example.training.incident_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.training.incident_management.model.Incident;

public interface IncidentRepository
        extends JpaRepository<Incident, Long> {

    Page<Incident> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable);

}