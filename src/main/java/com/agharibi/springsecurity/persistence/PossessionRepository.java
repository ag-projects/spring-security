package com.agharibi.springsecurity.persistence;

import com.agharibi.springsecurity.model.Possession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PossessionRepository extends JpaRepository<Possession, Long>{

    Possession findByName(String name);
}
