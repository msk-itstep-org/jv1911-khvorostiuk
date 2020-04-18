package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.repository.AudioRecordRepository;
import org.itstep.msk.app.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AudioRecordSearchServiceImpl implements SearchService<AudioRecord> {
    private final AudioRecordRepository audioRecordRepository;

    @Autowired
    public AudioRecordSearchServiceImpl(AudioRecordRepository audioRecordRepository) {
        this.audioRecordRepository = audioRecordRepository;
    }

    @Override
    public Set<AudioRecord> findByName(String name) {
        return audioRecordRepository.findByAuthorLikeOrNameLike("%" + name + "%", "%" + name + "%");
    }

    @Override
    public List<AudioRecord> findAll() {
        return audioRecordRepository.findAll();
    }
}
