package com.example.knowledgevault.model;

import com.example.knowledgevault.model.Knowledge;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("LINK")
public class LinkKnowledge extends Knowledge {

    private String url;
    private String description;
}
