package com.example.knowledgevault.controller;

import com.example.knowledgevault.dto.CreateKnowledgeRequest;
import com.example.knowledgevault.dto.KnowledgeResponse;
import com.example.knowledgevault.exception.ConflictException;
import com.example.knowledgevault.exception.GlobalExceptionHandler;
import com.example.knowledgevault.exception.NotFoundException;
import com.example.knowledgevault.service.KnowledgeService;
import com.example.knowledgevault.utils.KnowledgeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Import(GlobalExceptionHandler.class)

public class KnowledgeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private KnowledgeService knowledgeService;

    @InjectMocks
    private KnowledgeController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void recent_WithDefaultDays_Returns200AndResponses() throws Exception {

        when(knowledgeService.findCreatedAfter(any(LocalDateTime.class)))
                .thenReturn(List.of(new KnowledgeResponse()));

        mockMvc.perform(get("/api/knowledge/recent")
                        .param("days", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }


    @Test
    void getById_WhenIdExists_Returns200AndResponse() throws Exception {
        // Arrange
        Long id = 1L;
        KnowledgeResponse response = new KnowledgeResponse();
        when(knowledgeService.getById(id)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/knowledge/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getById_WhenIdDoesNotExist_Returns404() throws Exception {
        // Arrange
        Long id = 1L;
        when(knowledgeService.getById(id)).thenThrow(new NotFoundException("Knowledge not found"));

        // Act & Assert
        mockMvc.perform(get("/api/knowledge/{id}", id))
                .andExpect(status().isNotFound());
    }




    @Test
    void create_WithDuplicateTitle_Returns409() throws Exception {
        // Arrange
        CreateKnowledgeRequest request = new CreateKnowledgeRequest();
        request.setTitle("New Title");
        request.setType(KnowledgeType.  LINK);
        when(knowledgeService.create(any(CreateKnowledgeRequest.class)))
                .thenThrow(new ConflictException("Knowledge title already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/knowledge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "title": "Existing Title",
              "type": "LINK"
            }
        """))
                .andExpect(status().isConflict());

    }

    @Test
    void delete_WhenIdExists_Returns204() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(knowledgeService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api/knowledge/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_WhenIdDoesNotExist_Returns404() throws Exception {
        // Arrange
        Long id = 1L;
        doThrow(new NotFoundException("Knowledge not found")).when(knowledgeService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api/knowledge/{id}", id))
                .andExpect(status().isNotFound());
    }
}
