package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class FriendController {
    private final FriendService friendService;

    private final UserRepository userRepository;

    @Autowired
    public FriendController(FriendService friendService, UserRepository userRepository) {
        this.friendService = friendService;
        this.userRepository = userRepository;
    }

    @GetMapping("/addFriend/{id}")
    @ResponseBody
    public Map<String, Object> addFriend(@PathVariable("id") User userToAdd, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());

        friendService.add(user, userToAdd);

        Map<String, Object> result = new HashMap<>();
        result.put("status", friendService.getFriendStatus(user, userToAdd));
        result.put("id", "js-delete-friend");
        result.put("href", "/deleteFriend/" + userToAdd.getId());
        result.put("action", userToAdd.getFriends().contains(user) ? "Удалить из друзей" : "Отписаться");

        return result;
    }

    @GetMapping("/deleteFriend/{id}")
    @ResponseBody
    public Map<String, Object> deleteFriend(@PathVariable("id") User userToDelete, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());

        friendService.remove(user, userToDelete);

        Map<String, Object> result = new HashMap<>();
        result.put("status", friendService.getFriendStatus(user, userToDelete));
        result.put("id", "js-add-friend");
        result.put("href", "/addFriend/" + userToDelete.getId());
        result.put("action", "Добавить друга");

        return result;
    }

}
