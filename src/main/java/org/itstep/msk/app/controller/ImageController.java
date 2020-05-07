package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.Image;
import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.UnsupportedMediaTypeException;
import org.itstep.msk.app.repository.PostRepository;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.impl.AvatarServiceUploadImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {
    @Autowired
    private AvatarServiceUploadImpl avatarService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/profile_avatar")
    public String avatar(Authentication authentication,
                         @RequestParam("file") MultipartFile file) throws Exception {
        User user = userRepository.findByUsername(authentication.getName());
        Image avatar;
        Image oldAvatar = user.getAvatar();

        try {
            avatar = avatarService.upload(file);
            user.setAvatar(avatar);

            userRepository.save(user);
            userRepository.flush();

            if (oldAvatar != null) {
                avatarService.remove(oldAvatar);
            }

        } catch (UnsupportedMediaTypeException e) {
            return "redirect:/profile?uploaderror=true";
        }

        return "redirect:/profile";
    }

    @PostMapping("/add_post_picture/{id}")
    private String addPicture(@PathVariable("id") Post post,
                              @RequestParam("file") MultipartFile file) throws Exception {

        try {
            Image image = avatarService.upload(file);

            post.getImages().add(image);

            postRepository.save(post);
            postRepository.flush();


        } catch (UnsupportedMediaTypeException e) {
            return "redirect:/edit_post?uploadInPostError=true";
        }

        return "redirect:/edit_post/" + post.getId();
    }
}
