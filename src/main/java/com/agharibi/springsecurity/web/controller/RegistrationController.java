package com.agharibi.springsecurity.web.controller;

import com.agharibi.springsecurity.model.User;
import com.agharibi.springsecurity.persistence.UserRepository;
import com.agharibi.springsecurity.utils.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderUtil util;

    @RequestMapping(value = "signup")
    public ModelAndView  registrationForm() {
        return new ModelAndView("registrationPage", "user", new User());
    }

    @RequestMapping(value = "/user/register")
    public ModelAndView registerUser(@Valid final User user, final BindingResult result) {
        if(result.hasErrors()) {
            return new ModelAndView("registrationPage", "user", user);
        }

        user.setPassword(util.encode(user.getPassword()));
        user.setPasswordConfirmation(util.encode(user.getPasswordConfirmation()));

        userRepository.save(user);
        return new ModelAndView("redirect:/login");

    }


}
