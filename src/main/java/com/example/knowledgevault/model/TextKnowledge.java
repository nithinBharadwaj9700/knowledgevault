package com.example.knowledgevault.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@DiscriminatorValue("TEXT")
public class TextKnowledge extends Knowledge{

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;


}
