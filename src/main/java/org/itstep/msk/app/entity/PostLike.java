package org.itstep.msk.app.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "post_likes")
public class PostLike extends Like {
    @ManyToOne(targetEntity = Post.class)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
