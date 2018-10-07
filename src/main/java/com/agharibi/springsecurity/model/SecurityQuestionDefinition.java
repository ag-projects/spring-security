package com.agharibi.springsecurity.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class SecurityQuestionDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotEmpty
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SecurityQuestionDefinition{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

}
