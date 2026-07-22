package com.example.training.incident_management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.training.incident_management.dto.IncidentCreateRequest;
import com.example.training.incident_management.dto.IncidentResponse;
import com.example.training.incident_management.dto.IncidentUpdateRequest;
import com.example.training.incident_management.exception.IncidentNotFoundException;
import com.example.training.incident_management.model.Incident;
import com.example.training.incident_management.model.IncidentPriority;
import com.example.training.incident_management.model.IncidentStatus;
import com.example.training.incident_management.repository.IncidentRepository;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentService incidentService;

    @BeforeEach
    void beforeEach() {

        System.out.println("================================");
        System.out.println("テスト開始");
        System.out.println("================================");
    }

    @AfterEach
    void afterEach() {

        System.out.println("================================");
        System.out.println("テスト終了");
        System.out.println("================================");
    }

    @Test
    void 存在するID_検索_取得成功() {

        System.out.println("存在するID_検索_取得成功 開始");

        Incident incident = new Incident(
                1L,
                "ログインできない",
                "テスト",
                IncidentStatus.OPEN,
                IncidentPriority.HIGH,
                "sato",
                LocalDateTime.now());

        when(incidentRepository.findById(1L))
                .thenReturn(Optional.of(incident));

        Incident result =
                incidentService.findById(1L);

        System.out.println(
                "取得タイトル=" + result.getTitle());

        assertNotNull(result);
        assertEquals("ログインできない",
                result.getTitle());

        verify(incidentRepository)
                .findById(1L);

        System.out.println("存在するID_検索_取得成功 完了");
    }

    @Test
    void 存在しないID_検索_例外発生() {

        System.out.println("存在しないID_検索_例外発生 開始");

        when(incidentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                IncidentNotFoundException.class,
                () -> incidentService.findById(999L));

        verify(incidentRepository)
                .findById(999L);

        System.out.println("例外発生確認OK");
    }

    @Test
    void 正常データ_登録_登録成功() {

        System.out.println("正常データ_登録_登録成功 開始");

        IncidentCreateRequest request =
                new IncidentCreateRequest();

        request.setTitle("障害A");
        request.setDescription("説明");
        request.setPriority(IncidentPriority.HIGH);
        request.setAssignee("sato");
        request.setOccurredAt(LocalDateTime.now());

        Incident savedIncident =
                new Incident(
                        1L,
                        "障害A",
                        "説明",
                        IncidentStatus.OPEN,
                        IncidentPriority.HIGH,
                        "sato",
                        request.getOccurredAt());

        when(incidentRepository.save(any()))
                .thenReturn(savedIncident);

        IncidentResponse response =
                incidentService.create(request);

        System.out.println(
                "登録タイトル=" + response.getTitle());

        assertEquals(
                "障害A",
                response.getTitle());

        verify(incidentRepository)
                .save(any(Incident.class));

        System.out.println("登録成功");
    }

    @Test
    void 存在するデータ_削除_削除成功() {

        System.out.println("存在するデータ_削除_削除成功 開始");

        when(incidentRepository.existsById(1L))
                .thenReturn(true);

        boolean result =
                incidentService.delete(1L);

        System.out.println(
                "削除結果=" + result);

        assertTrue(result);

        verify(incidentRepository)
                .deleteById(1L);

        System.out.println("削除成功");
    }

    @Test
    void 存在しないデータ_削除_削除失敗() {

        System.out.println("存在しないデータ_削除_削除失敗 開始");

        when(incidentRepository.existsById(999L))
                .thenReturn(false);

        boolean result =
                incidentService.delete(999L);

        System.out.println(
                "削除結果=" + result);

        assertFalse(result);

        verify(incidentRepository,
                never()).deleteById(anyLong());

        System.out.println("削除対象なし確認OK");
    }
    
    @Test
    void 不具合B_更新時_updatedAt更新_更新日時変更() {

        LocalDateTime originalTime =
                LocalDateTime.of(
                        2026,
                        7,
                        22,
                        10,
                        0);

        Incident incident = new Incident();

        incident.setId(1L);
        incident.setTitle("Before");
        incident.setUpdatedAt(originalTime);

        when(incidentRepository.findById(1L))
                .thenReturn(Optional.of(incident));

        when(incidentRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        IncidentUpdateRequest request =
                new IncidentUpdateRequest();

        request.setTitle("After");
        request.setDescription("Updated");
        request.setStatus(
                IncidentStatus.IN_PROGRESS);
        request.setPriority(
                IncidentPriority.HIGH);
        request.setAssignee("sato");
        request.setOccurredAt(
                LocalDateTime.now());

        incidentService.update(1L, request);

        assertTrue(
                incident.getUpdatedAt()
                        .isAfter(originalTime));
    }
    @Test
    void 不具合A_存在しない担当者_検索_0件返却() {

        Page<Incident> emptyPage =
                new PageImpl<>(
                        List.of());

        when(
            incidentRepository
                .findByAssigneeContainingIgnoreCase(
                        "XXXXX",
                        PageRequest.of(0, 20)))
                .thenReturn(emptyPage);

        Page<Incident> result =
                incidentService.findAll(
                        null,
                        "XXXXX",
                        PageRequest.of(0, 20));

        assertNotNull(result);
        assertEquals(
                0,
                result.getTotalElements());
    }
}