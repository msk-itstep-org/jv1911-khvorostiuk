package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.ForbiddenException;
import org.itstep.msk.app.repository.PostRepository;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(UserRepository userRepository,
                           PostRepository postRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addPost(User user, Post post) {
        post.setUser(user);
        user.getPosts().add(post);

        postRepository.save(post);
        postRepository.flush();
    }

    @Override
    public void deletePost(User user, Post post) {
        user.getPosts().remove(post);

        userRepository.save(user);
        userRepository.flush();

    }

    @Override
    public void editPost(Post post, Post editedPost) {

        post.setPostMessage(editedPost.getPostMessage());

        postRepository.save(post);
        postRepository.flush();

    }
}
