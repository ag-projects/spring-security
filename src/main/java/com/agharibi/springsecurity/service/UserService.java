package com.agharibi.springsecurity.service;

import com.agharibi.springsecurity.model.User;

public interface UserService {

    /**
     * @param user
     * @return
     */
    User registerNewUser(User user);
}
