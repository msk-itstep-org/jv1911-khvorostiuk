package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.ForbiddenException;
import org.itstep.msk.app.exceptions.UnsupportedMediaTypeException;
import org.itstep.msk.app.repository.AudioRecordRepository;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.impl.AudioRecordSearchServiceImpl;
import org.itstep.msk.app.service.impl.AudioRecordUploadServiceImpl;
import org.itstep.msk.app.service.impl.AudioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AudioController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AudioRecordRepository audioRecordRepository;

    @Autowired
    private AudioRecordUploadServiceImpl audioRecordService;

    @Autowired
    private AudioRecordSearchServiceImpl audioRecordSearchService;

    @Autowired
    private AudioServiceImpl audioService;

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

    @GetMapping("/audio_list")
    public String audioList(Authentication authentication, Model model,
                            @RequestParam(required = false, defaultValue = "") String name) throws Exception {
        User user = userRepository.findByUsername(authentication.getName());

        if (name.length() == 0) {
            model.addAttribute("records", audioRecordSearchService.findAll());
        } else {
            model.addAttribute("records", audioRecordSearchService.findByName(name));
        }

        model.addAttribute("user", user);

        return "audio_list";
    }

    @GetMapping("/audio_add/{id}")
    public String audioAdd(Authentication authentication,
                           @PathVariable(name = "id") AudioRecord audioRecord) {
        User user = userRepository.findByUsername(authentication.getName());

        audioService.add(user, audioRecord);

        return "redirect:/audioList";
    }


    @GetMapping("/delete_audio/{id}")
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

    @GetMapping("/edit_post/{id}/add-audio/{audioId}")
    private String addAudio(@PathVariable("id") Post post,
                            @PathVariable("audioId") AudioRecord audioRecord) {

        audioService.addToPost(post, audioRecord);

        return "redirect:/editPost/" + post.getId();
    }
}
