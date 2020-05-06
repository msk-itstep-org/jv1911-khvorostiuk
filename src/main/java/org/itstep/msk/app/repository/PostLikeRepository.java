package org.itstep.msk.app.repository;

import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.PostLike;
import org.itstep.msk.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByUserAndPost(User user, Post post);
}
