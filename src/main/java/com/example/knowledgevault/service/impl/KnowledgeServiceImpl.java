package com.example.knowledgevault.service.impl;

import com.example.knowledgevault.dto.CreateKnowledgeRequest;
import com.example.knowledgevault.dto.KnowledgeResponse;
import com.example.knowledgevault.exception.ConflictException;
import com.example.knowledgevault.exception.NotFoundException;
import com.example.knowledgevault.mapper.KnowledgeMapper;
import com.example.knowledgevault.model.Knowledge;
import com.example.knowledgevault.repository.KnowledgeRepository;
import com.example.knowledgevault.service.KnowledgeService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@Transactional
public class KnowledgeServiceImpl implements KnowledgeService {

    private final KnowledgeRepository repository;
    private final KnowledgeMapper knowledgeMapper;

    public KnowledgeServiceImpl(KnowledgeRepository repository, KnowledgeMapper knowledgeMapper) {
        this.repository = repository;
        this.knowledgeMapper = knowledgeMapper;
    }

    @Override
    public KnowledgeResponse create(CreateKnowledgeRequest request) {

        log.info("Creating knowledge title={}, type={}",
                request.getTitle(), request.getType());

        repository.findByTitle(request.getTitle())
                .ifPresent(k -> {
                    throw new ConflictException("Knowledge title already exists");
                });
        Knowledge knowledge = knowledgeMapper.toEntity(request);
        knowledge.setUpdatedBy("System");
        knowledge.setCreatedBy("System");
        knowledge.setUpdatedAt(LocalDateTime.now());
        knowledge.setCreatedAt(LocalDateTime.now());

        Knowledge saved = repository.save(knowledge);


        return knowledgeMapper.toResponse(saved);
    }

    @Override
    public KnowledgeResponse getById(Long id) {
        Knowledge knowledge = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Knowledge not found"));
        return knowledgeMapper.toResponse(knowledge);
    }

    public List<KnowledgeResponse> findCreatedAfter(LocalDateTime from) {

        return repository.findCreatedAfter(from)
                .stream()
                .map(knowledgeMapper::toResponse)
                .toList();
    }

    @Override
    public Page<KnowledgeResponse> listAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(knowledgeMapper::toResponse);
    }


    @Override
    public void delete(Long id) {
        Knowledge knowledge = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Knowledge not found"));
        repository.delete(knowledge);
    }



}
