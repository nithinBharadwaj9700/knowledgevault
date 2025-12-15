package com.example.knowledgevault.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("QUOTE")
public class QuoteKnowledge extends Knowledge {

    private String quote;
    private String author;
}
