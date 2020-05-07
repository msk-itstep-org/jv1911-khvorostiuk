package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.PostLike;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.NotFoundException;
import org.itstep.msk.app.repository.PostLikeRepository;
import org.itstep.msk.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @PostMapping("/post_like/{id}")
    public Boolean postLike(
            @PathVariable(name = "id") Post post,
            Authentication authentication
    ) {
        if (post == null) {
            throw new NotFoundException();
        }

        User user = userRepository.findByUsername(authentication.getName());
        PostLike like = postLikeRepository.findByUserAndPost(
                user,
                post
        );

        if (like == null) {
            like = new PostLike();
            like.setUser(user);
            like.setPost(post);
            postLikeRepository.save(like);
            postLikeRepository.flush();

            return true;
        }

        postLikeRepository.delete(like);
        postLikeRepository.flush();

        return false;
    }
}
