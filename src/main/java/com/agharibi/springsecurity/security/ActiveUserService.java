package com.agharibi.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActiveUserService {

    @Autowired
    private SessionRegistry sessionRegistry;

    public List<String> getAllActiveUsers() {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        User[] users = principals.toArray(new User[principals.size()]);

        return Arrays.stream(users)
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
                .map(u -> u.getUsername())
                .collect(Collectors.toList());
    }
}
