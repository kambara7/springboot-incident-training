package com.example.training.incident_management.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.training.incident_management.dto.IncidentCreateRequest;
import com.example.training.incident_management.dto.IncidentResponse;
import com.example.training.incident_management.dto.IncidentUpdateRequest;
import com.example.training.incident_management.model.Incident;
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
                .orElse(null);
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
            Pageable pageable) {

        if (title == null || title.isBlank()) {
            return incidentRepository.findAll(pageable);
        }

        return incidentRepository
                .findByTitleContainingIgnoreCase(
                        title,
                        pageable);
    }
}