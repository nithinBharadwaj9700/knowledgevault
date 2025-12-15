package com.example.knowledgevault.dto;

import com.example.knowledgevault.utils.KnowledgeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.List;


@Getter
@Setter
public class CreateKnowledgeRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9 ]+$")
    private String title;
    @NotNull
    private KnowledgeType type;
    @Pattern(regexp = "^[A-Za-z0-9 ]+$")
    private String textContent;
    @URL(message = "Invalid URL format")
    private String url;
    private String description;
    private String quote;
    private String author;
    private List<Long> childIds;
}
