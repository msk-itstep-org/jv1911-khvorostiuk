package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.ForbiddenException;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {
    private final PostServiceImpl postService;

    private final UserRepository userRepository;

    @Autowired
    public PostController(PostServiceImpl postService, UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @GetMapping("/addPost")
    public String addPost(Authentication authentication, Post post) {
        User user = userRepository.findByUsername(authentication.getName());

        postService.addPost(user, post);

        return "redirect:/editPost/" + post.getId();
    }

    @GetMapping("/editPost/{id}")
    public String editPost(Authentication authentication,
                           @PathVariable("id") Post post, Model model,
                           @RequestParam(defaultValue = "false", value = "uploaderror") String error) {
        User user = userRepository.findByUsername(authentication.getName());

        if (!user.equals(post.getUser())) {
            throw new ForbiddenException();
        }

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("error", error.equalsIgnoreCase("true"));

        return "editPost";
    }

    @PostMapping("/editPost/{id}")
    private String savePost(Authentication authentication,
                            @PathVariable("id") Post post,
                            @ModelAttribute Post editedPost) {
        User user = userRepository.findByUsername(authentication.getName());

        if (!user.equals(post.getUser())) {
            throw new ForbiddenException();
        }

        postService.editPost(post, editedPost);

        return "redirect:/profile";
    }

}
