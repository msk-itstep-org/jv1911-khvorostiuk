package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

}