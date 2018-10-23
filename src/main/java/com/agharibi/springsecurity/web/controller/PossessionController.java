package com.agharibi.springsecurity.web.controller;


import com.agharibi.springsecurity.model.Possession;
import com.agharibi.springsecurity.persistence.PossessionRepository;
import com.agharibi.springsecurity.persistence.UserRepository;
import com.agharibi.springsecurity.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/possession")
public class PossessionController {

    @Autowired
    private PossessionRepository possessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    @PostAuthorize("hasPermission(returnObject, 'READ') or hasPermission(returnObject, 'ADMINISTRATION')")
    public Possession findOne(@PathVariable("id") final Long id) {
        return possessionRepository.getOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(@Valid Possession possession, Authentication authentication) {
        possession.setOwner(userRepository.findByEmail(authentication.getName()));
        possession = possessionRepository.save(possession);
        System.out.println(possession);

        permissionService.addPermissionForUser(possession, BasePermission.ADMINISTRATION, authentication.getName());
        return new ModelAndView("redirect:/user?message=Possession created with id " + possession.getId());
    }

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(@ModelAttribute final Possession possession) {
        return "users/possession";
    }
}
