package com.example.training.incident_management.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class SetupCheckControllerTest {

    private static final Logger log = LoggerFactory.getLogger(SetupCheckControllerTest.class);

    @Autowired
    private WebApplicationContext context;

    @Test
    void setupCheck_returnsStatus() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        var result = mockMvc.perform(get("/setup-check"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();

        // ★ ログ出力
        log.info("HTTPステータス = {}", result.getResponse().getStatus());
        log.info("レスポンス本文 = {}", body);

        // ★ JSON の中身を簡易チェック
        assertThat(body).contains("incident_management");
    }
}
