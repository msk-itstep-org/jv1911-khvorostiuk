package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void add(User currentUser, User user) {
        if (currentUser.equals(user)) {
            return;
        } else if (currentUser.getFriends().contains(user)) {
            return;
        }

        currentUser.getFriends().add(user);
        userRepository.save(currentUser);
        userRepository.flush();
    }

    @Override
    public void remove(User currentUser, User user) {
        currentUser.getFriends().remove(user);

        userRepository.save(user);
        userRepository.flush();
    }

    @Override
    public String getFriendStatus(User currentUser, User user) {
        if (!currentUser.getFriends().contains(user)) {
            if (!user.getFriends().contains(currentUser)) {
                return "";
            } else {
                return user.getUsername() + " подписан на Вас";
            }
        } else {
            if (!user.getFriends().contains(currentUser)) {
                return "Вы подписаны на " + user.getUsername();
            } else {
                return user.getUsername() + " у Вас в друзьях";
            }
        }
    }
}
