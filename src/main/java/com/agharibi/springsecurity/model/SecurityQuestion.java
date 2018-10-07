package com.agharibi.springsecurity.model;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Optional;

@Entity
public class SecurityQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", unique = true)
    private User user;

    @OneToOne(targetEntity = SecurityQuestionDefinition.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "securityQuestionDefinition_id")
    private SecurityQuestionDefinition questionDefinition;

    private String answer;

    public SecurityQuestion() {
    }

    public SecurityQuestion(@Valid User user, Optional<SecurityQuestionDefinition> questionDefinition, String answer) {
        this.user = user;
        this.questionDefinition = questionDefinition.orElse(new SecurityQuestionDefinition());
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SecurityQuestionDefinition getQuestionDefinition() {
        return questionDefinition;
    }

    public void setQuestionDefinition(SecurityQuestionDefinition questionDefinition) {
        this.questionDefinition = questionDefinition;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecurityQuestion)) return false;

        SecurityQuestion that = (SecurityQuestion) o;

        if (getUser() != null ? !getUser().equals(that.getUser()) : that.getUser() != null) return false;
        if (getQuestionDefinition() != null ? !getQuestionDefinition().equals(that.getQuestionDefinition()) : that.getQuestionDefinition() != null)
            return false;
        return getAnswer() != null ? getAnswer().equals(that.getAnswer()) : that.getAnswer() == null;
    }

    @Override
    public int hashCode() {
        int result = getUser() != null ? getUser().hashCode() : 0;
        result = 31 * result + (getQuestionDefinition() != null ? getQuestionDefinition().hashCode() : 0);
        result = 31 * result + (getAnswer() != null ? getAnswer().hashCode() : 0);
        return result;
    }
}
