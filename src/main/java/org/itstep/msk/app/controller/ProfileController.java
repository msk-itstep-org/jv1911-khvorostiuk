package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {
    private final UserRepository userRepository;

    private final FriendService friendService;

    @Autowired
    public ProfileController(UserRepository userRepository, FriendService friendService) {
        this.userRepository = userRepository;
        this.friendService = friendService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/profile")
    public String userPage(@RequestParam(defaultValue = "false", value = "uploaderror") String error,
                           @RequestParam(defaultValue = "false", value = "wrongExtension") String wrongExtension,
                           Authentication authentication, Model model, Post post) {
        User user = userRepository.findByUsername(authentication.getName());

        model.addAttribute("user", user);
        model.addAttribute("error", error.equalsIgnoreCase("true"));
        model.addAttribute("wrongExtension", wrongExtension.equalsIgnoreCase("true"));
        model.addAttribute("post", post);

        return "profile";
    }

    @GetMapping("/profile/{id}")
    private String userPageStranger(@PathVariable(name = "id") User user, Model model, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("friendStatus", friendService.getFriendStatus(currentUser, user));

        return "strangerProfile";
    }

}
