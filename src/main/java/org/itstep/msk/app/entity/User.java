package org.itstep.msk.app.entity;

import org.itstep.msk.app.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "Имя пользователя не может быть пустым")
    private String username;

    @Column
    @NotEmpty(message = "Пароль не может быть пустым")
    private String password;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Column
    private boolean active;

    @Column
    @NotEmpty(message = "Адрес электронной почты не может быть пустым")
    private String email;

    @Column(name = "activation_code")
    private String activationCode;

    @OneToOne(targetEntity = Avatar.class)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private Avatar avatar;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friends = new HashSet<>();

    @ManyToMany(mappedBy = "friends")
    private Set<User> followers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_records",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "audio_id")
    )
    private Set<AudioRecord> audioRecords = new HashSet<>();

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }

        return this.getId().equals(((User) obj).getId());
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Set<String> getStringRoles() {
        return roles.stream().map(Enum::toString).collect(Collectors.toSet());
    }

    public void setStringRoles(Set<String> stringRoles) {
        roles.clear();
        for (String stringRole : stringRoles) {
            roles.add(Role.valueOf(stringRole));
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public Set<AudioRecord> getAudioRecords() {
        return audioRecords;
    }
}
