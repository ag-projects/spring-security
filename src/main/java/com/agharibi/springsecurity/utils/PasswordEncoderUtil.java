package com.agharibi.springsecurity.utils;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderUtil {

    private PasswordEncoder encoder;

    /**
     * @param rawPassword
     * @return encoded password
     */
    public String encode(String rawPassword) {
        this.encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return this.encoder.encode(rawPassword);
    }

}
