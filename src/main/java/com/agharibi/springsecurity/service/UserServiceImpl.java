package com.agharibi.springsecurity.service;

import com.agharibi.springsecurity.model.User;
import com.agharibi.springsecurity.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerNewUser(final User user) {
        if (emailExist(user.getEmail())) {
            throw new IllegalArgumentException("There is an account with that email address: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    private boolean emailExist(String email) {
        final User user = userRepository.findByEmail(email);
        return user != null;
    }
}
