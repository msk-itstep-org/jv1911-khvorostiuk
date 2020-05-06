package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;

public interface PostService {
    void addPost(User user, Post post);

    void deletePost(User user, Post post);

    void editPost(Post post, Post editedPost);
}
