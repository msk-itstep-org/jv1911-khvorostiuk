package org.itstep.msk.app.controller;

import org.itstep.msk.app.enums.Role;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.MyMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Controller
public class SecurityController {
    @Autowired
    private MyMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(@RequestParam(defaultValue = "false") String error, Model model) {
        model.addAttribute("error", error.equalsIgnoreCase("true"));
        return "login";
    }

    @GetMapping("/registration")
    public String reg(@RequestParam(defaultValue = "false") String sameUser, Model model) {
        model.addAttribute("sameUser", sameUser.equalsIgnoreCase("true"));


        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute User user, Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }

        User sameUser = userRepository.findByUsername(user.getUsername());

        if (sameUser != null) {
            return "redirect:/registration?userFromDb=true";
        }

        user.setActive(true);
        user.getRoles().add(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);
        userRepository.flush();

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Приветствую, %s \n" + "Добро пожаловать!" +
                            " Для завершения регистрации перейди по ссылке: http://localhost:9999/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Код активации", message);
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        if (activateUser(code)){
            model.addAttribute("message","Регистрация подстверждена");
        } else {
            model.addAttribute("message","Код подтверждения не найден!");
        }

        return "login";
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        userRepository.save(user);
        userRepository.flush();

        return true;
    }

}
