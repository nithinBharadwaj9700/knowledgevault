package com.example.knowledgevault.model;

import com.example.knowledgevault.model.Knowledge;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("NESTED")
public class NestedKnowledge extends Knowledge {

    @ManyToMany
    private List<Knowledge> children;
}
