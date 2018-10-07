package com.agharibi.springsecurity.persistence;


import com.agharibi.springsecurity.model.SecurityQuestionDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityQuestionDefinitionRepository extends JpaRepository<SecurityQuestionDefinition, Long> {

}
