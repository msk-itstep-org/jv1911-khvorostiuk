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
    private final UserRepository userRepository;

    private final AudioRecordRepository audioRecordRepository;

    private final AudioRecordUploadServiceImpl audioRecordService;

    private final AudioRecordSearchServiceImpl audioRecordSearchService;

    private final AudioServiceImpl audioService;

    @Autowired
    public AudioController(UserRepository userRepository, AudioRecordRepository audioRecordRepository, AudioRecordUploadServiceImpl audioRecordService, AudioRecordSearchServiceImpl audioRecordSearchService, AudioServiceImpl audioService) {
        this.userRepository = userRepository;
        this.audioRecordRepository = audioRecordRepository;
        this.audioRecordService = audioRecordService;
        this.audioRecordSearchService = audioRecordSearchService;
        this.audioService = audioService;
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

    @GetMapping("/editPost/{id}/{audioId}")
    private String addAudio(@PathVariable("id") Post post,
                            @PathVariable("audioId") AudioRecord audioRecord) {

        audioService.addToPost(post, audioRecord);

        return "redirect:/editPost/" + post.getId();
    }
}
