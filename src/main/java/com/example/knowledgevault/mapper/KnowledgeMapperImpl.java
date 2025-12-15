package com.example.knowledgevault.mapper;

import com.example.knowledgevault.dto.CreateKnowledgeRequest;
import com.example.knowledgevault.dto.KnowledgeChildResponse;
import com.example.knowledgevault.dto.KnowledgeResponse;
import com.example.knowledgevault.exception.BadRequestException;
import com.example.knowledgevault.model.*;
import com.example.knowledgevault.repository.KnowledgeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class KnowledgeMapperImpl implements KnowledgeMapper{
    @Value("${app.max-nested-items}")
    private int maxNestedItems;
    private final KnowledgeRepository repository;

    public KnowledgeMapperImpl(KnowledgeRepository repository) {
        this.repository = repository;
    }

    public Knowledge toEntity(CreateKnowledgeRequest request) {

        Knowledge knowledge = switch (request.getType()) {

            case TEXT -> {
                TextKnowledge text = new TextKnowledge();
                text.setContent(request.getTextContent());
                yield text;
            }

            case LINK -> {
                LinkKnowledge link = new LinkKnowledge();
                link.setUrl(request.getUrl());
                link.setDescription(request.getDescription());
                yield link;
            }

            case QUOTE -> {
                QuoteKnowledge quote = new QuoteKnowledge();
                quote.setQuote(request.getQuote());
                quote.setAuthor(request.getAuthor());
                yield quote;
            }

            case NESTED -> {
                NestedKnowledge nested = new NestedKnowledge();
                nested.setTitle(request.getTitle());

                List<Long> childIds = Optional.ofNullable(request.getChildIds())
                        .orElse(List.of());

                if (childIds.size() > maxNestedItems) {
                    throw new BadRequestException(
                            "Maximum " + maxNestedItems + " nested items allowed");
                }

                List<Knowledge> children = repository.findAllById(childIds);
                nested.setChildren(children);
                yield nested;
            }


        };

        knowledge.setTitle(request.getTitle());

        return knowledge;
    }


    public KnowledgeResponse toResponse(Knowledge knowledge) {
        KnowledgeResponse response = new KnowledgeResponse();
        response.setId(knowledge.getId());
        response.setTitle(knowledge.getTitle());
        response.setType(knowledge.getClass().getSimpleName().toUpperCase());
        if (knowledge instanceof LinkKnowledge link) {
            response.setUrl(link.getUrl());
            response.setDescription(link.getDescription());
        }
        if (knowledge instanceof QuoteKnowledge quoteKnowledge) {
            response.setQuote(quoteKnowledge.getQuote());
            response.setAuthor(quoteKnowledge.getAuthor());
        }

        if (knowledge instanceof TextKnowledge textKnowledge) {
            response.setTextContent(textKnowledge.getContent());
        }

        if (knowledge instanceof NestedKnowledge nested) {
            List<KnowledgeChildResponse> children =
                    nested.getChildren().stream()
                            .map(child -> {
                                KnowledgeChildResponse c = new KnowledgeChildResponse();
                                c.setId(child.getId());
                                c.setTitle(child.getTitle());
                                c.setType(child.getClass().getSimpleName().toUpperCase());
                                return c;
                            })
                            .toList();

            response.setChild(children);
        }
        return response;
    }


}
