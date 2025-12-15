package com.example.knowledgevault.mapper;

import com.example.knowledgevault.dto.CreateKnowledgeRequest;
import com.example.knowledgevault.dto.KnowledgeResponse;
import com.example.knowledgevault.model.Knowledge;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface KnowledgeMapper {

    Knowledge toEntity(CreateKnowledgeRequest request);
    KnowledgeResponse toResponse(Knowledge knowledge);
}
