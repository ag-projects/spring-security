package com.agharibi.springsecurity.web.model;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.Calendar;

public class User {

    private Long id;

//    @NotEmpty(message = "UserName is required")
//    private String username;

    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required.")
    private String password;

    @NotEmpty(message = "Password confirmation is required.")
    private String passwordConfirmation;

    private Calendar created = Calendar.getInstance();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }
}
