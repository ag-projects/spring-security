package com.agharibi.springsecurity.persistence;

import com.agharibi.springsecurity.model.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Long> {

    SecurityQuestion findByQuestionDefinitionIdAndUserIdAndAnswer(Long questionDefinitionId, Long userId, String answer);

    SecurityQuestion findByUserIdAndAnswer(Long id, String answer);
}
