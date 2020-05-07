package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.Community;
import org.itstep.msk.app.entity.Image;
import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.ForbiddenException;
import org.itstep.msk.app.exceptions.UnsupportedMediaTypeException;
import org.itstep.msk.app.repository.CommunityRepository;
import org.itstep.msk.app.repository.PostRepository;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.CommunityService;
import org.itstep.msk.app.service.impl.AvatarServiceUploadImpl;
import org.itstep.msk.app.service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CommunityController {
    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private AvatarServiceUploadImpl avatarService;

    @GetMapping("/add_community")
    private String addCommunity(Authentication authentication, Community community) {
        User user = userRepository.findByUsername(authentication.getName());

        communityService.create(community, user);

        return "redirect:/editCommunity/" + community.getId();
    }

    @GetMapping("/editCommunity/{id}")
    private String editCommunity(Authentication authentication,
                                 @PathVariable("id") Community community,
                                 Model model,
                                 @RequestParam(defaultValue = "false", value = "uploaderror") String error) {
        User user = userRepository.findByUsername(authentication.getName());

        if (!user.equals(community.getOwner())) {
            throw new ForbiddenException();
        }

        model.addAttribute("user", user);
        model.addAttribute("community", community);
        model.addAttribute("error", error);

        return "edit_community";
    }

    @PostMapping("/save_community/{id}")
    private String saveCommunity(Authentication authentication,
                                 @PathVariable("id") Community community,
                                 @ModelAttribute Community editedCommunity) {
        User user = userRepository.findByUsername(authentication.getName());

        if (!user.equals(community.getOwner())) {
            throw new ForbiddenException();
        }

        communityService.edit(community, editedCommunity);

        return "redirect:/community";
    }

    @PostMapping("/add_community_picture/{id}")
    private String addPicture(@PathVariable("id") Community community,
                              @RequestParam("file") MultipartFile file) throws Exception {

        try {
            Image image = avatarService.upload(file);

            community.setAvatar(image);

            communityRepository.save(community);
            communityRepository.flush();


        } catch (UnsupportedMediaTypeException e) {
            return "redirect:/community?uploadInPostError=true";
        }

        return "redirect:/edit_community/" + community.getId();
    }

    @PostMapping("/add_community_post/{communityId}/add_post/{postId}")
    private String addCommunityPost(@PathVariable("communityId") Community community,
                                    @PathVariable("postId") Post post,
                                    Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());

        postService.addCommunityPost(community, post);

        return "redirect:/editCommunity/" + community.getId();
    }

}
