package com.agharibi.springsecurity.service;

import com.agharibi.springsecurity.model.User;
import com.agharibi.springsecurity.model.VerificationToken;

public interface UserService {

    /**
     * @param user
     * @return
     */
    User registerNewUser(User user);

    /**
     * @param email
     * @return
     */
    User findUserByEmail(final String email);

    /**
     * @param user
     * @param token
     */
    void createVerificationTokenForUser(User user, String token);

    /**
     * @param token
     * @return
     */
    VerificationToken getVerificationToken(String token);

    /**
     * @param user
     */
    void saveRegisteredUser(User user);

}
