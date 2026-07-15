package com.example.training.incident_management.conteoller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class SetupCheckControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Test
    void setupCheckReturnsOk() throws Exception {

        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        String result = mockMvc.perform(get("/setup-check"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo("Setup OK");
    }
}
