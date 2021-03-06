package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.ForbiddenException;
import org.itstep.msk.app.repository.AudioRecordRepository;
import org.itstep.msk.app.repository.PostRepository;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class AudioServiceImpl implements AudioService {

    private final UserRepository userRepository;

    private final AudioRecordRepository audioRecordRepository;

    private final PostRepository postRepository;

    public AudioServiceImpl(@Autowired AudioRecordRepository audioRecordRepository,
                            @Autowired UserRepository userRepository,
                            @Autowired PostRepository postRepository) {
        this.audioRecordRepository = audioRecordRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void add(User user, AudioRecord audioRecord) {

        if (!user.getAudioRecords().contains(audioRecord)) {
            user.getAudioRecords().add(audioRecord);
        } else {
            throw new ForbiddenException();
        }

        audioRecordRepository.save(audioRecord);
        audioRecordRepository.flush();
        userRepository.save(user);
        userRepository.flush();
    }

    @Override
    public void addToPost(Post post, AudioRecord audioRecord) {

        if (!post.getAudioRecords().contains(audioRecord)) {
            post.getAudioRecords().add(audioRecord);
        } else {
            throw new ForbiddenException();
        }

        postRepository.save(post);
        postRepository.flush();
    }


    @Override
    public String getAudioStatus(User user, AudioRecord audioRecord) {
        if (!user.getAudioRecords().contains(audioRecord)) {
            return "Добавить";
        } else {
            return "Добавлено";
        }
    }
}
