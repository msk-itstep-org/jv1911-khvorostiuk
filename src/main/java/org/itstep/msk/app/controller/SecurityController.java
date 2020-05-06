package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.NotFoundException;
import org.itstep.msk.app.service.MailSender;
import org.itstep.msk.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SecurityController {
    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false, defaultValue = "false") String error,
            HttpServletRequest request,
            Authentication authentication,
            Model model
    ) {
        if (authentication != null) {
            return "redirect:/profile";
        }

        boolean hasRegistrationSuccess = false;
        boolean hasActivationSuccess = false;
        boolean hasLoginError = error.equalsIgnoreCase("true");
        Map<String, ?> flashes = RequestContextUtils.getInputFlashMap(request);
        if (flashes != null) {
            hasRegistrationSuccess = flashes.containsKey("hasRegistrationSuccess")
                    ? (Boolean) flashes.get("hasRegistrationSuccess")
                    : hasRegistrationSuccess;
            hasActivationSuccess = flashes.containsKey("hasActivationSuccess")
                    ? (Boolean) flashes.get("hasActivationSuccess")
                    : hasActivationSuccess;
            hasLoginError = flashes.containsKey("hasLoginError")
                    ? (Boolean) flashes.get("hasLoginError")
                    : hasLoginError;
        }

        model.addAttribute("user", new User());
        model.addAttribute("hasRegistrationSuccess", hasRegistrationSuccess);
        model.addAttribute("hasActivationSuccess", hasActivationSuccess);
        model.addAttribute("hasLoginError", hasLoginError);

        return "security/login";
    }

    @GetMapping("/registration")
    @SuppressWarnings("unchecked")
    public String registration(HttpServletRequest request, Authentication authentication, Model model) {
        if (authentication != null) {
            return "redirect:/profile";
        }

        Map<String, List<String>> errors = new HashMap<>();
        boolean hasSameUsernameError = false;
        Map<String, ?> flashes = RequestContextUtils.getInputFlashMap(request);
        if (flashes != null) {
            errors = flashes.containsKey("errors")
                    ? (Map<String, List<String>>) flashes.get("errors")
                    : errors;
            hasSameUsernameError = flashes.containsKey("hasSameUsernameError")
                    ? (Boolean) flashes.get("hasSameUsernameError")
                    : hasSameUsernameError;
        }

        model.addAttribute("user", new User());
        model.addAttribute("errors", errors);
        model.addAttribute("hasSameUsernameError", hasSameUsernameError);

        return "security/registration";
    }

    @PostMapping("/registration")
    public String register(
            @Valid @ModelAttribute User registeringUser,
            BindingResult bindingResult,
            HttpServletRequest request,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) throws Exception {
        if (authentication != null) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                if (!errors.containsKey(error.getField())) {
                    errors.put(error.getField(), new ArrayList<>());
                }

                errors.get(error.getField()).add(error.getDefaultMessage());
            }

            redirectAttributes.addFlashAttribute("errors", errors);

            return "redirect:/registration";
        }

        if (userService.isUsernameExists(registeringUser.getUsername())) {
            redirectAttributes.addFlashAttribute("hasSameUsernameError", true);

            return "redirect:/registration";
        }

        User user = userService.createUser(
                registeringUser.getUsername(),
                registeringUser.getPassword(),
                registeringUser.getEmail()
        );
        userService.save(user);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", user.getUsername());
        parameters.put(
                "activationLink",
                String.format(
                        "http://%s%s/activate/%s/%s",
                        request.getLocalName(),
                        request.getLocalPort() != 80 ? ":" + request.getServerPort() : "",
                        user.getId(),
                        user.getActivationCode()
                )
        );
        mailSender.send(user.getEmail(), "Подтверждение почты", "security/activation_mail.html", parameters);

        redirectAttributes.addFlashAttribute("hasRegistrationSuccess", true);

        return "redirect:/login";
    }

    @GetMapping("/activate/{userId}/{activationCode}")
    public String activate(
            @PathVariable("userId") User user,
            @PathVariable("activationCode") String activationCode,
            RedirectAttributes redirectAttributes
    ) {
        if (!userService.activate(user, activationCode)) {
            throw new NotFoundException();
        }

        redirectAttributes.addFlashAttribute("hasActivationSuccess", true);

        return "redirect:/login";
    }
}
