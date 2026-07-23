package com.example.training.incident_management.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.training.incident_management.dto.IncidentResponse;
import com.example.training.incident_management.exception.IncidentNotFoundException;
import com.example.training.incident_management.model.Incident;
import com.example.training.incident_management.model.IncidentPriority;
import com.example.training.incident_management.model.IncidentStatus;
import com.example.training.incident_management.service.IncidentService;

@WebMvcTest(IncidentController.class)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentService incidentService;

    @Test
    void shouldFindIncidentById() throws Exception {

        Incident incident = new Incident(
                1L,
                "ログインできない",
                "利用者から問い合わせ",
                IncidentStatus.OPEN,
                IncidentPriority.HIGH,
                "田中",
                LocalDateTime.now());

        when(incidentService.findById(1L))
                .thenReturn(incident);

        mockMvc.perform(get("/api/incidents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("ログインできない"));
    }
    
    @Test
    void shouldFindAllIncidents() throws Exception {

        Incident incident = new Incident(
                1L,
                "ログインできない",
                "利用者から問い合わせ",
                IncidentStatus.OPEN,
                IncidentPriority.HIGH,
                "田中",
                LocalDateTime.now());

        Page<Incident> page = new PageImpl<>(
                List.of(incident),
                PageRequest.of(0, 20),
                1);

        when(incidentService.findAll(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id")
                        .value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("ログインできない"));
    }
    
    @Test
    void shouldCreateIncident() throws Exception {

        IncidentResponse response = new IncidentResponse();

        response.setId(1L);
        response.setTitle("New Incident");
        response.setDescription("Create test");

        when(incidentService.create(any()))
                .thenReturn(response);

        String requestBody = """
                {
                  "title":"New Incident",
                  "description":"Create test",
                  "priority":"HIGH",
                  "assignee":"Tanaka",
                  "occurredAt":"2026-07-22T09:00:00"
                }
                """;

        mockMvc.perform(
                post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("New Incident"));
    }
    
    @Test
    void shouldUpdateIncident() throws Exception {

        IncidentResponse response = new IncidentResponse();

        response.setId(1L);
        response.setTitle("Updated Incident");
        response.setDescription("Updated description");

        when(incidentService.update(
                eq(1L),
                any()))
                .thenReturn(response);

        String requestBody = """
                {
                  "title":"Updated Incident",
                  "description":"Updated description",
                  "status":"IN_PROGRESS",
                  "priority":"HIGH",
                  "assignee":"Tanaka",
                  "occurredAt":"2026-07-22T09:00:00"
                }
                """;

        mockMvc.perform(
                put("/api/incidents/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Updated Incident"));
    }
    
    @Test
    void shouldDeleteIncident() throws Exception {

        when(incidentService.delete(1L))
                .thenReturn(true);

        mockMvc.perform(delete("/api/incidents/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void shouldReturnBadRequestWhenValidationFails()
            throws Exception {

    	String requestBody = """
    	        {
    	          "priority":"HIGH",
    	          "assignee":"Tanaka",
    	          "occurredAt":"2026-07-22T09:00:00"
    	        }
    	        """;

        mockMvc.perform(
                post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.message")
                        .value("タイトルは必須です"));
    }
    
    @Test
    void shouldReturnNotFoundWhenIncidentDoesNotExist()
            throws Exception {

        when(incidentService.findById(999L))
                .thenThrow(
                        new IncidentNotFoundException(
                                "インシデントが存在しません。id=999"));

        mockMvc.perform(get("/api/incidents/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.message")
                        .value("インシデントが存在しません。id=999"));
    }
}