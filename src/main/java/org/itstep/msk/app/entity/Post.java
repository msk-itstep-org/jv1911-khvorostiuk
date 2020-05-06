package org.itstep.msk.app.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "posts")
public class Post {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String postMessage;

    @ManyToMany()
    @JoinTable(
            name = "post_picture",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "picture_id"))
    private Set<Image> images = new HashSet<>();

    @ManyToMany()
    @JoinTable(
            name = "post_audio",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "audio_id"))
    private Set<AudioRecord> audioRecords = new HashSet<>();

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Long getId() {
        return id;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public Set<Image> getImages() {
        return images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<AudioRecord> getAudioRecords() {
        return audioRecords;
    }

}
