package com.example.knowledgevault.controller;

import com.example.knowledgevault.dto.KnowledgeResponse;
import com.example.knowledgevault.service.KnowledgeService;
import com.example.knowledgevault.config.SecurityConfig;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(KnowledgeController.class)
@Import(SecurityConfig.class)
class KnowledgeControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KnowledgeService knowledgeService;

    @Test
    void delete_WithoutAuthentication_Returns401() throws Exception {
        mockMvc.perform(delete("/api/knowledge/{id}", 1L)
                .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void create_WithUserRole_Returns403() throws Exception {
        mockMvc.perform(post("/api/knowledge")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "title": "Test",
                      "type": "LINK"
                    }
                """))
                .andExpect(status().isCreated());
    }

    @Test
    void create_WithAdminRole_Returns201() throws Exception {

        when(knowledgeService.create(any()))
                .thenReturn(new KnowledgeResponse());

        mockMvc.perform(post("/api/knowledge")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "title": "Test",
                      "type": "LINK"
                    }
                """))
                .andExpect(status().isCreated());
    }
}
