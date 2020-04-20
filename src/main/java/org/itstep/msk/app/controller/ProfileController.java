package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.entity.Avatar;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.ForbiddenException;
import org.itstep.msk.app.exceptions.UnsupportedMediaTypeException;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.FriendService;
import org.itstep.msk.app.service.impl.AudioRecordSearchServiceImpl;
import org.itstep.msk.app.service.impl.AudioRecordUploadServiceImpl;
import org.itstep.msk.app.service.impl.AvatarServiceUploadImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ProfileController {
    @Autowired
    private AvatarServiceUploadImpl avatarService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AudioRecordUploadServiceImpl audioRecordService;

    @Autowired
    private AudioRecordSearchServiceImpl audioRecordSearchService;

    @Autowired
    private FriendService friendService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/profile")
    public String userPage(@RequestParam(defaultValue = "false", value = "uploaderror") String error,
                           @RequestParam(defaultValue = "false", value = "uploaderror") String wrongExtension,
                           Authentication authentication, Model model) {
        User user = userRepository.findByUsername(authentication.getName());

        model.addAttribute("user", user);
        model.addAttribute("error", error.equalsIgnoreCase("true"));
        model.addAttribute("wrongExtension", wrongExtension.equalsIgnoreCase("true"));

        return "profile";
    }

    @GetMapping("/profile/{id}")
    private String userPageStranger(@PathVariable(name = "id") User user, Model model, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("friendStatus", friendService.getFriendStatus(currentUser, user));

        return "strangerProfile";
    }

    @PostMapping("/profile")
    public String avatar(Authentication authentication,
                         @RequestParam("file") MultipartFile file) throws Exception {
        User user = userRepository.findByUsername(authentication.getName());
        Avatar avatar;
        Avatar oldAvatar = user.getAvatar();

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

    @PostMapping("/audio")
    public String audio(Authentication authentication,
                        @RequestParam("audio_file") MultipartFile file) throws Exception {
        User user = userRepository.findByUsername(authentication.getName());
        AudioRecord audioRecord;

        try {
            audioRecord = audioRecordService.upload(file);
            user.getAudioRecords().add(audioRecord);

            userRepository.save(user);
            userRepository.flush();

        } catch (UnsupportedMediaTypeException e) {
            return "redirect:/profile?uploaderror=true";
        }

        return "redirect:/profile";
    }

    @GetMapping("/audioList")
    public String audioList(Authentication authentication, Model model,
                            @RequestParam(required = false, defaultValue = "") String name) throws Exception {
        User user = userRepository.findByUsername(authentication.getName());

        if (name.length() == 0) {
            model.addAttribute("records", audioRecordSearchService.findAll());
        } else {
            model.addAttribute("records", audioRecordSearchService.findByName(name));
        }

        model.addAttribute("user", user);

        return "audioList";
    }


    @GetMapping("/deleteAudio/{id}")
    public String audioDel(Authentication authentication, @PathVariable("id") AudioRecord audioRecord) throws Exception {
        User user = userRepository.findByUsername(authentication.getName());

        if (!user.getAudioRecords().contains(audioRecord)) {
            throw new ForbiddenException();
        }

        user.getAudioRecords().remove(audioRecord);

        userRepository.save(user);
        userRepository.flush();

        return "redirect:/profile";
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
