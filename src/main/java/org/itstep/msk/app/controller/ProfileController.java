package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.entity.Image;
import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.ForbiddenException;
import org.itstep.msk.app.exceptions.UnsupportedMediaTypeException;
import org.itstep.msk.app.repository.AudioRecordRepository;
import org.itstep.msk.app.repository.PostRepository;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.FriendService;
import org.itstep.msk.app.service.impl.*;
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
    private AudioServiceImpl audioService;

    @Autowired
    private AvatarServiceUploadImpl avatarService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AudioRecordRepository audioRecordRepository;

    @Autowired
    private AudioRecordUploadServiceImpl audioRecordService;

    @Autowired
    private AudioRecordSearchServiceImpl audioRecordSearchService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostServiceImpl postService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/profile")
    public String userPage(@RequestParam(defaultValue = "false", value = "uploaderror") String error,
                           @RequestParam(defaultValue = "false", value = "wrongExtension") String wrongExtension,
                           Authentication authentication, Model model, Post post) {
        User user = userRepository.findByUsername(authentication.getName());

        model.addAttribute("user", user);
        model.addAttribute("error", error.equalsIgnoreCase("true"));
        model.addAttribute("wrongExtension", wrongExtension.equalsIgnoreCase("true"));
        model.addAttribute("post", post);

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
        Image avatar;
        Image oldAvatar = user.getAvatar();

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
                        @RequestParam("audio_file") MultipartFile file,
                        @RequestParam("audio_author") String author,
                        @RequestParam("audio_name") String name) throws Exception {
        User user = userRepository.findByUsername(authentication.getName());
        AudioRecord audioRecord;

        try {
            audioRecord = audioRecordService.upload(file);
            user.getAudioRecords().add(audioRecord);
            audioRecord.setAuthor(author);
            audioRecord.setName(name);

            audioRecordRepository.save(audioRecord);
            audioRecordRepository.flush();

            userRepository.save(user);
            userRepository.flush();

        } catch (UnsupportedMediaTypeException e) {
            return "redirect:/profile?wrongExtension=true";
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

    @GetMapping("/audioAdd/{id}")
    public String audioAdd(Authentication authentication,
                           @PathVariable(name = "id") AudioRecord audioRecord) {
        User user = userRepository.findByUsername(authentication.getName());

        audioService.add(user, audioRecord);

//        Map<String, Object> result = new HashMap<>();
//        result.put("status", audioService.getAudioStatus(user, audioRecord));
//        result.put("id", "js-add-audio");
//        result.put("href", "/audioAdd/");
//        result.put("action", user.getAudioRecords().contains(audioRecord) ? "Добавить" : "Добавлено");

        return "redirect:/audioList";
    }


    @GetMapping("/deleteAudio/{id}")
    public String audioDel(Authentication authentication, @PathVariable("id") AudioRecord audioRecord) throws
            Exception {
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

    @GetMapping("/addPost")
    public String addPost(Authentication authentication, Post post) {
        User user = userRepository.findByUsername(authentication.getName());

        postService.addPost(user, post);

        return "redirect:/editPost/" + post.getId();
    }

    @GetMapping("/editPost/{id}")
    public String editPost(Authentication authentication,
                           @PathVariable("id") Post post, Model model,
                           @RequestParam(defaultValue = "false", value = "uploaderror") String error) {
        User user = userRepository.findByUsername(authentication.getName());

        if (!user.equals(post.getUser())) {
            throw new ForbiddenException();
        }

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("error", error.equalsIgnoreCase("true"));

        return "editPost";
    }

    @PostMapping("/editPost/{id}")
    private String savePost(Authentication authentication,
                            @PathVariable("id") Post post,
                            @ModelAttribute Post editedPost) {
        User user = userRepository.findByUsername(authentication.getName());

        if (!user.equals(post.getUser())) {
            throw new ForbiddenException();
        }

        postService.editPost(post, editedPost);

        return "redirect:/profile";
    }

    @PostMapping("/addPostPicture/{id}")
    private String addPicture(@PathVariable("id") Post post,
                              @RequestParam("file") MultipartFile file) throws Exception {

        try {
            Image image = avatarService.upload(file);

            post.getImages().add(image);

            postRepository.save(post);
            postRepository.flush();


        } catch (UnsupportedMediaTypeException e) {
            return "redirect:/editPost?uploadInPostError=true";
        }

        return "redirect:/editPost/" + post.getId();
    }

    @GetMapping("/editPost/{id}/{audioId}")
    private String addAudio(@PathVariable("id") Post post,
                            @PathVariable("audioId") AudioRecord audioRecord) {

        audioService.addToPost(post, audioRecord);

        return "redirect:/editPost/" + post.getId();
    }
}
