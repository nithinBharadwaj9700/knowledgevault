package com.example.knowledgevault.service;

import com.example.knowledgevault.dto.CreateKnowledgeRequest;
import com.example.knowledgevault.dto.KnowledgeResponse;
import com.example.knowledgevault.exception.ConflictException;
import com.example.knowledgevault.exception.NotFoundException;
import com.example.knowledgevault.mapper.KnowledgeMapper;
import com.example.knowledgevault.model.Knowledge;
import com.example.knowledgevault.model.TextKnowledge;
import com.example.knowledgevault.repository.KnowledgeRepository;
import com.example.knowledgevault.service.impl.KnowledgeServiceImpl;
import com.example.knowledgevault.utils.KnowledgeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KnowledgeServiceImplTest {

    @Mock
    private KnowledgeRepository repository;

    @Mock
    private KnowledgeMapper knowledgeMapper;

    @InjectMocks
    private KnowledgeServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create_WhenTitleExists_ThrowsConflictException() {

        CreateKnowledgeRequest request = new CreateKnowledgeRequest();
        request.setTitle("Existing Title");
        request.setType(KnowledgeType.TEXT);

        Knowledge existingKnowledge = new TextKnowledge();
        existingKnowledge.setId(1L);
        existingKnowledge.setTitle("Existing Title");

        when(repository.findByTitle("Existing Title"))
                .thenReturn(Optional.of(existingKnowledge));

        assertThrows(ConflictException.class, () -> service.create(request));

        // Optional: verify interaction
        verify(repository).findByTitle("Existing Title");
        verify(repository, never()).save(any());
    }


    @Test
    void create_WhenValidRequest_SavesAndReturnsResponse() {

        CreateKnowledgeRequest request = new CreateKnowledgeRequest();
        request.setTitle("New Title");
        request.setType(KnowledgeType.TEXT);
        request.setTextContent("Some text");

        Knowledge entity = new TextKnowledge();

        when(repository.findByTitle("New Title"))
                .thenReturn(Optional.empty());
        when(knowledgeMapper.toEntity(request))
                .thenReturn(entity);
        when(repository.save(entity))
                .thenReturn(entity);

        KnowledgeResponse response = new KnowledgeResponse();
        when(knowledgeMapper.toResponse(entity))
                .thenReturn(response);

        KnowledgeResponse result = service.create(request);
        assertEquals(response, result);
        assertEquals("System", entity.getCreatedBy());
        assertEquals("System", entity.getUpdatedBy());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());

        verify(repository).findByTitle("New Title");
        verify(repository).save(entity);
    }


    @Test
    void getById_WhenIdExists_ReturnsResponse() {
        Long id = 1L;
        Knowledge entity = new TextKnowledge();

        when(repository.findById(id))
                .thenReturn(Optional.of(entity));

        KnowledgeResponse response = new KnowledgeResponse();
        when(knowledgeMapper.toResponse(entity))
                .thenReturn(response);


        KnowledgeResponse result = service.getById(id);


        assertEquals(response, result);
    }


    @Test
    void getById_WhenIdDoesNotExist_ThrowsNotFoundException() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getById(1L));
    }


    @Test
    void findCreatedAfter_ReturnsMappedList() {
        LocalDateTime from = LocalDateTime.now();

        Knowledge k1 = new TextKnowledge();
        Knowledge k2 = new TextKnowledge();

        when(repository.findCreatedAfter(from))
                .thenReturn(List.of(k1, k2));

        KnowledgeResponse r1 = new KnowledgeResponse();
        KnowledgeResponse r2 = new KnowledgeResponse();

        when(knowledgeMapper.toResponse(k1)).thenReturn(r1);
        when(knowledgeMapper.toResponse(k2)).thenReturn(r2);

        List<KnowledgeResponse> result = service.findCreatedAfter(from);

        assertEquals(2, result.size());
        assertEquals(r1, result.get(0));
        assertEquals(r2, result.get(1));
    }


    @Test
    void listAll_ReturnsPageOfResponses() {
        Pageable pageable = PageRequest.of(0, 10);

        Knowledge k1 = new TextKnowledge();
        Knowledge k2 = new TextKnowledge();

        Page<Knowledge> page =
                new PageImpl<>(List.of(k1, k2));

        when(repository.findAll(pageable))
                .thenReturn(page);

        KnowledgeResponse r1 = new KnowledgeResponse();
        KnowledgeResponse r2 = new KnowledgeResponse();

        when(knowledgeMapper.toResponse(k1)).thenReturn(r1);
        when(knowledgeMapper.toResponse(k2)).thenReturn(r2);
        Page<KnowledgeResponse> result = service.listAll(pageable);
        assertEquals(2, result.getContent().size());
        assertEquals(r1, result.getContent().get(0));
        assertEquals(r2, result.getContent().get(1));
    }


    @Test
    void delete_WhenIdExists_DeletesEntity() {
        Long id = 1L;
        Knowledge entity = new TextKnowledge();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        service.delete(id);
        verify(repository, times(1)).delete(entity);
    }

    @Test
    void delete_WhenIdDoesNotExist_ThrowsNotFoundException() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.delete(id));
    }
}
