package com.example.knowledgevault.service;

import com.example.knowledgevault.dto.CreateKnowledgeRequest;
import com.example.knowledgevault.dto.KnowledgeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


public interface KnowledgeService {

    KnowledgeResponse getById(Long id);

    List<KnowledgeResponse> findCreatedAfter(LocalDateTime from);
    Page<KnowledgeResponse> listAll(Pageable pageable);

    KnowledgeResponse create(CreateKnowledgeRequest request);

    void delete(Long id);
}

