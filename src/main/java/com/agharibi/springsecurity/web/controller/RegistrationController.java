package com.agharibi.springsecurity.web.controller;

import com.agharibi.springsecurity.model.*;
import com.agharibi.springsecurity.persistence.SecurityQuestionDefinitionRepository;
import com.agharibi.springsecurity.persistence.SecurityQuestionRepository;
import com.agharibi.springsecurity.registration.OnRegistrationCompleteEvent;
import com.agharibi.springsecurity.security.UserDetailsServiceImpl;
import com.agharibi.springsecurity.service.UserService;
import com.agharibi.springsecurity.utils.PasswordEncoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class RegistrationController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoderUtil util;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private SecurityQuestionDefinitionRepository securityQuestionDefinitionRepository;

    @Autowired
    private SecurityQuestionRepository securityQuestionRepository;

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm() {

        Map<String, Object> model = new HashMap<>();
        model.put("user", new User());
        model.put("questions", securityQuestionDefinitionRepository.findAll());

        ModelAndView modelAndView = new ModelAndView("registrationPage", model);
        return modelAndView;
    }

    /**
     * register user
     */

    @RequestMapping(value = "user/register")
    public ModelAndView regiterUser(@Valid final User user, @RequestParam Long questionId, @RequestParam String answer, final BindingResult result, final HttpServletRequest request) {
        if (result.hasErrors()) {
            return new ModelAndView("registrationPage", "user", user);
        }
        try {
            String encodedPass = this.util.encode(user.getPassword());
            user.setPassword(encodedPass);
            user.setPasswordConfirmation(this.util.encode(user.getPasswordConfirmation()));

            User registeredUser = userService.registerNewUser(user);

            Optional<SecurityQuestionDefinition> questionDefinition = securityQuestionDefinitionRepository.findById(questionId);
            securityQuestionRepository.save(new SecurityQuestion(user, questionDefinition, answer));

            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(appUrl, registeredUser));

        }catch (Exception e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("registrationPage", "user", user);
        }
        return new ModelAndView("loginPage");
    }

    @RequestMapping(value = "/registrationConfirm")
    public ModelAndView confirmRegistration(final Model model, @RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {
        final VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid account confirmation token.");
            return new ModelAndView("redirect:/login");
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your registration token has expired. Please register again.");
            return new ModelAndView("redirect:/login");
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        redirectAttributes.addFlashAttribute("message", "Your account verified successfully");
        return new ModelAndView("redirect:/login");
    }

    /**
     *  password reset
     */

    @ResponseBody
    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
    public ModelAndView resetPassword(HttpServletRequest request, @RequestParam("email") String userEamil, RedirectAttributes redirectAttributes) {
        User user = userService.findUserByEmail(userEamil);
        if(user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            SimpleMailMessage email = constructResetTokenEmail(appUrl, token, user);
            mailSender.send(email);
        }
        redirectAttributes.addFlashAttribute("message", "You should receive an Password Reset Email shortly");
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
    public ModelAndView showChangePasswordPage(@RequestParam("id") long id, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        PasswordResetToken passToken = userService.getPasswordResetToken(token);
        if(passToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset token");
            return new ModelAndView("redirect:/login");
        }
        User user = passToken.getUser();
        if(user.getId() != id) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset token");
            return new ModelAndView("redirect:/login");
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your password reset token has expired");
            return new ModelAndView("redirect:/login");
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
                userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return new ModelAndView("resetPassword");
    }

    @ResponseBody
    @RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
    public ModelAndView savePassword(@RequestParam("password") String password,
                                     @RequestParam("passwordConfirmation") String passwordConfirmation,
                                     @RequestParam Long questionId,
                                     @RequestParam String answer,
                                     RedirectAttributes redirectAttributes) {

        if(!password.equals(passwordConfirmation)) {
            Map<String, Object> model = new HashMap<>();
            model.put("errorMessage", "Passwords do not match");
            model.put("questions", securityQuestionDefinitionRepository.findAll());
            return new ModelAndView("resetPassword", model);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(securityQuestionRepository.findByQuestionDefinitionIdAndUserIdAndAnswer(questionId, user.getId(), answer) == null) {
            Map<String, Object> model = new HashMap<>();
            model.put("errorMessage", "Answer to security question is incorrect");
            model.put("questions", securityQuestionDefinitionRepository.findAll());
            return new ModelAndView("resetPassword", model);
        }

        String encodedPassword = this.util.encode(password);
        userService.changeUserPassword(user, encodedPassword);
        redirectAttributes.addFlashAttribute("message", "Password reset successfully");
        return new ModelAndView("redirect:/login");
    }

    private SimpleMailMessage constructResetTokenEmail(String contextPath, String token, User user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText("Please open the following URL to reset your password: \r\n" + url);
        email.setFrom("user@email.com");

        return email;
    }


}
