package com.example.training.incident_management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.training.incident_management.model.Incident;
import com.example.training.incident_management.model.IncidentPriority;
import com.example.training.incident_management.model.IncidentStatus;

@Service
public class IncidentService {

    private final List<Incident> incidents = List.of(

        new Incident(
                1L,
                "ログインできない",
                "利用者から問い合わせ",
                IncidentStatus.OPEN,
                IncidentPriority.HIGH,
                "神原",
                LocalDateTime.now()),

        new Incident(
                2L,
                "画面が表示されない",
                "ブラウザエラー",
                IncidentStatus.IN_PROGRESS,
                IncidentPriority.MEDIUM,
                "かんばら",
                LocalDateTime.now())
    );

    /**
     * 一覧取得
     */
    public List<Incident> findAll() {
        return incidents;
    }

    /**
     * ID指定取得
     */
    public Incident findById(Long id) {

        return incidents.stream()
                .filter(incident ->
                        incident.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}