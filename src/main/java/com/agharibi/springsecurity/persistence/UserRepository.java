package com.agharibi.springsecurity.persistence;

import com.agharibi.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * @param email
     * @return
     */
    User findByEmail(String email);

}
