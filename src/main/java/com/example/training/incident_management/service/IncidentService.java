package com.example.training.incident_management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log =
            LoggerFactory.getLogger(
                    IncidentService.class);

    private final IncidentRepository incidentRepository;

    public IncidentService(
            IncidentRepository incidentRepository) {

        this.incidentRepository =
                incidentRepository;
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

    public IncidentResponse create(
            IncidentCreateRequest request) {

        log.info(
                "登録開始 status={}",
                IncidentStatus.OPEN);

        Incident incident = new Incident();

        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setPriority(request.getPriority());
        incident.setAssignee(request.getAssignee());
        incident.setOccurredAt(request.getOccurredAt());

        incident.setStatus(IncidentStatus.OPEN);

        Incident savedIncident =
                incidentRepository.save(incident);

        log.info(
                "登録完了 incidentId={} status={}",
                savedIncident.getId(),
                savedIncident.getStatus());

        return toResponse(savedIncident);
    }
    
//    //ERROR試験用
//    public IncidentResponse create(
//            IncidentCreateRequest request) {
//
//        log.info(
//                "登録開始 status={}",
//                IncidentStatus.OPEN);
//
//        try {
//
//            throw new RuntimeException(
//                    "ログ確認用例外");
//
//        } catch (Exception e) {
//
//            log.error(
//                    "登録失敗",
//                    e);
//
//            throw e;
//        }
//    }

    public IncidentResponse update(
            Long id,
            IncidentUpdateRequest request) {

        log.info(
                "更新開始 incidentId={}",
                id);

        Incident incident =
                incidentRepository.findById(id)
                        .orElse(null);

        if (incident == null) {

            log.warn(
                    "更新対象なし incidentId={}",
                    id);

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

        log.info(
                "更新完了 incidentId={} status={}",
                savedIncident.getId(),
                savedIncident.getStatus());

        return toResponse(savedIncident);
    }

    public boolean delete(Long id) {

        log.info(
                "削除開始 incidentId={}",
                id);

        if (!incidentRepository.existsById(id)) {

            log.warn(
                    "削除対象なし incidentId={}",
                    id);

            return false;
        }

        incidentRepository.deleteById(id);

        log.info(
                "削除完了 incidentId={} result=success",
                id);

        return true;
    }

    private IncidentResponse toResponse(
            Incident incident) {

        IncidentResponse response =
                new IncidentResponse();

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

        if (title != null
                && !title.isBlank()) {

            return incidentRepository
                    .findByTitleContainingIgnoreCase(
                            title,
                            pageable);
        }

        if (assignee != null
                && !assignee.isBlank()) {

            return incidentRepository
                    .findByAssigneeContainingIgnoreCase(
                            assignee,
                            pageable);
        }

        return incidentRepository
                .findAll(pageable);
    }

    public Page<Incident> findAll(
            String title,
            String assignee,
            IncidentStatus status,
            IncidentPriority priority,
            Pageable pageable) {

        if (title != null
                && !title.isBlank()) {

            return incidentRepository
                    .findByTitleContainingIgnoreCase(
                            title,
                            pageable);
        }

        if (assignee != null
                && !assignee.isBlank()) {

            return incidentRepository
                    .findByAssigneeContainingIgnoreCase(
                            assignee,
                            pageable);
        }

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

        return incidentRepository.findAll(
                pageable);
    }
}