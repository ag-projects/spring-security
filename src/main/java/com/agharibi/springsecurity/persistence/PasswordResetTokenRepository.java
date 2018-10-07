package com.agharibi.springsecurity.persistence;

import com.agharibi.springsecurity.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{

    PasswordResetToken findByToken(String token);
}
