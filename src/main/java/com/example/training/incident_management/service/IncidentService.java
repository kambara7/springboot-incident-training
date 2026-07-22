package com.example.training.incident_management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.training.incident_management.dto.IncidentCreateRequest;
import com.example.training.incident_management.dto.IncidentResponse;
import com.example.training.incident_management.dto.IncidentUpdateRequest;
import com.example.training.incident_management.exception.IncidentNotFoundException;
import com.example.training.incident_management.model.Incident;
import com.example.training.incident_management.model.IncidentPriority;
import com.example.training.incident_management.model.IncidentStatus;
import com.example.training.incident_management.repository.IncidentRepository;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(
            IncidentRepository incidentRepository) {

        this.incidentRepository = incidentRepository;
    }

    public List<Incident> findAll() {
        return incidentRepository.findAll();
    }

    public Incident findById(Long id) {

        return incidentRepository.findById(id)
                .orElseThrow(() ->
                        new IncidentNotFoundException(
                                "インシデントが存在しません。id=" + id));
    }

    public IncidentResponse create(IncidentCreateRequest request) {

        Incident incident = new Incident();

        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setPriority(request.getPriority());
        incident.setAssignee(request.getAssignee());
        incident.setOccurredAt(request.getOccurredAt());

        incident.setStatus(IncidentStatus.OPEN);

        Incident savedIncident =
                incidentRepository.save(incident);

        return toResponse(savedIncident);
    }
    
    public IncidentResponse update(
            Long id,
            IncidentUpdateRequest request) {

        Incident incident =
                incidentRepository.findById(id)
                        .orElse(null);

        if (incident == null) {
            return null;
        }

        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setStatus(request.getStatus());
        incident.setPriority(request.getPriority());
        incident.setAssignee(request.getAssignee());
        incident.setOccurredAt(request.getOccurredAt());

        // Defect B Fix
        incident.setUpdatedAt(LocalDateTime.now());

        Incident savedIncident =
                incidentRepository.save(incident);

        return toResponse(savedIncident);
    }
    
    public boolean delete(Long id) {

        if (!incidentRepository.existsById(id)) {
            return false;
        }

        incidentRepository.deleteById(id);

        return true;
    }

    private IncidentResponse toResponse(Incident incident) {

        IncidentResponse response = new IncidentResponse();

        response.setId(incident.getId());
        response.setTitle(incident.getTitle());
        response.setDescription(incident.getDescription());
        response.setStatus(incident.getStatus());
        response.setPriority(incident.getPriority());
        response.setAssignee(incident.getAssignee());
        response.setOccurredAt(incident.getOccurredAt());

        return response;
    }
    
    public Page<Incident> findAll(
            String title,
            String assignee,
            Pageable pageable) {

        if (title != null && !title.isBlank()) {

            return incidentRepository
                    .findByTitleContainingIgnoreCase(
                            title,
                            pageable);
        }

        if (assignee != null
                && !assignee.isBlank()) {

        	Page<Incident> result =
        	        incidentRepository
        	                .findByAssigneeContainingIgnoreCase(
        	                        assignee,
        	                        pageable);

        	return result;
        }

        return incidentRepository.findAll(
                pageable);
    }
    
    public Page<Incident> findAll(
            String title,
            String assignee,
            IncidentStatus status,
            IncidentPriority priority,
            Pageable pageable) {

        if (title != null && !title.isBlank()) {

            return incidentRepository
                    .findByTitleContainingIgnoreCase(
                            title,
                            pageable);
        }

        if (assignee != null && !assignee.isBlank()) {

            return incidentRepository
                    .findByAssigneeContainingIgnoreCase(
                            assignee,
                            pageable);
        }

        // Defect C
        if (status != null) {

            return incidentRepository
                    .findByStatus(
                            status,
                            pageable);
        }

        if (priority != null) {

            return incidentRepository
                    .findByPriority(
                            priority,
                            pageable);
        }

        return incidentRepository.findAll(pageable);
    }

}