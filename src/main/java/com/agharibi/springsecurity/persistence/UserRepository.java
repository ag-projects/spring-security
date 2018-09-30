package com.agharibi.springsecurity.persistence;

import com.agharibi.springsecurity.web.model.User;

public interface UserRepository {

    Iterable<User> findAll();

    User save(User user);

    User findUser(Long id);

    void deleteUser(Long id);
}
