package com.example.training.incident_management.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SetupCheckServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SetupCheckServiceTest.class);

    @Autowired
    private SetupCheckService setupCheckService;

    @Test
    void getStatus_returnsAppNameAndTime() {

        Map<String, Object> result = setupCheckService.getStatus();

        log.info("app = {}", result.get("app"));
        log.info("time = {}", result.get("time"));

        assertThat(result.get("app")).isEqualTo("incident_management");
        assertThat(result.get("time")).isInstanceOf(LocalDateTime.class);
    }
}
