package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.entity.Avatar;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.ForbiddenException;
import org.itstep.msk.app.exceptions.UnsupportedContentTypeException;
import org.itstep.msk.app.repository.AudioRecordRepository;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.impl.AudioRecordServiceImpl;
import org.itstep.msk.app.service.impl.AvatarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ProfileController {
    @Autowired
    private AvatarServiceImpl avatarService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AudioRecordRepository audioRecordRepository;

    @Autowired
    private AudioRecordServiceImpl audioRecordService;

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
        model.addAttribute("wrongExtension", error.equalsIgnoreCase("true"));

        return "profile";
    }

    @PostMapping("/profile")
    public String avatar(Authentication authentication, @RequestParam("file") MultipartFile file) throws Exception {
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

        } catch (UnsupportedContentTypeException e) {
            return "redirect:/profile?uploaderror=true";
        }

        return "redirect:/profile";
    }

    @PostMapping("/audio")
    public String audio(Authentication authentication, @RequestParam("audio_file") MultipartFile file) throws Exception {
        User user = userRepository.findByUsername(authentication.getName());
        AudioRecord audioRecord;

        try {
            audioRecord = audioRecordService.upload(file);
            user.getAudioRecords().add(audioRecord);

            userRepository.save(user);
            userRepository.flush();

        } catch (UnsupportedContentTypeException e) {
            return "redirect:/profile?uploaderror=true";
        }

        return "redirect:/profile";
    }

    @PostMapping("/deleteAudio/{id}")
    public String audioDel(Authentication authentication, @PathVariable("id") AudioRecord audioRecord) throws Exception {
        User user = userRepository.findByUsername(authentication.getName());

        if (!user.getAudioRecords().contains(audioRecord)){
            throw new ForbiddenException();
        }

        user.getAudioRecords().remove(audioRecord);

        userRepository.save(user);
        userRepository.flush();

        return "redirect:/profile";
    }
}
