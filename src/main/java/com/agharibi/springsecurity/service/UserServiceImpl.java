package com.agharibi.springsecurity.service;

import com.agharibi.springsecurity.model.User;
import com.agharibi.springsecurity.model.VerificationToken;
import com.agharibi.springsecurity.persistence.UserRepository;
import com.agharibi.springsecurity.persistence.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerNewUser(final User user) {
        if (emailExist(user.getEmail())) {
            throw new IllegalArgumentException("There is an account with that email address: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    private boolean emailExist(String email) {
        final User user = userRepository.findByEmail(email);
        return user != null;
    }
}
