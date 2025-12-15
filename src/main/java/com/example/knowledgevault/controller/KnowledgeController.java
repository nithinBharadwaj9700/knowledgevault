package com.example.knowledgevault.controller;

import com.example.knowledgevault.dto.CreateKnowledgeRequest;
import com.example.knowledgevault.dto.KnowledgeResponse;
import com.example.knowledgevault.service.KnowledgeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService){
        this.knowledgeService = knowledgeService;
    }
    // public
    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(knowledgeService.getById(id));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<KnowledgeResponse>> recent(@RequestParam(defaultValue = "7") int days) {
        LocalDateTime from = LocalDateTime.now().minusDays(days);
        return ResponseEntity.ok(knowledgeService.findCreatedAfter(from));
    }


    public ResponseEntity<Page<KnowledgeResponse>> list(@PageableDefault(sort = "createdAt") Pageable pageable) {
        Page<KnowledgeResponse> result = knowledgeService.listAll(pageable);
        return ResponseEntity.ok(result);
    }

    // private
    @PostMapping
    public ResponseEntity<KnowledgeResponse> create(
            @Valid @RequestBody CreateKnowledgeRequest request) {

        KnowledgeResponse created = knowledgeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        knowledgeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
