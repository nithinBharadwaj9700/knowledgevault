package com.example.knowledgevault.dto;

import com.example.knowledgevault.model.Knowledge;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Setter
@Getter
public class KnowledgeResponse {
    private Long  id;
    private String title;
    private String type; // TEXT, LINK, QUOTE, NESTED
    private String textContent;
    private String url;
    private String description;
    private String quote;
    private String author;
    private List<KnowledgeChildResponse> child;
}
