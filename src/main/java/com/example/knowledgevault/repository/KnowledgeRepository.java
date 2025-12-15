package com.example.knowledgevault.repository;


import com.example.knowledgevault.model.Knowledge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {


    Page<Knowledge> findAll(Pageable pageable);


    Optional<Knowledge> findByTitle(String title);

    @Query("select k from Knowledge k where k.createdAt >= :from")
    List<Knowledge> findCreatedAfter(LocalDateTime from);
}
