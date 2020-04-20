package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.exceptions.UnsupportedMediaTypeException;
import org.itstep.msk.app.repository.AudioRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

@Service
public class AudioRecordUploadServiceImpl extends AbstractUploadService<AudioRecord> {
        final private String[] CONTENT_TYPES = {"audio/mp3", "audio/mp4"};
        private AudioRecordRepository audioRecordRepository;

        public AudioRecordUploadServiceImpl(@Autowired AudioRecordRepository audioRecordRepository,
                                            @Value("${upload.path}") String uploadsPath) {
            super(uploadsPath);
            this.audioRecordRepository = audioRecordRepository;
        }

        @Override
        public AudioRecord upload(MultipartFile file) throws Exception {
            String originalFileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
            String filename = generateUniqueFileName(originalFileName);

            if (!Arrays.asList(CONTENT_TYPES).contains(file.getContentType())) {
                throw new UnsupportedMediaTypeException();
            }

            file.transferTo(getUploadsPath().resolve(filename).toFile());

            AudioRecord audioRecord = new AudioRecord();
            audioRecord.setFilename(filename);
            audioRecord.setOriginalFilename(originalFileName);
            audioRecord.setContentType(file.getContentType());

            audioRecordRepository.save(audioRecord);
            audioRecordRepository.flush();

            return audioRecord;
        }

        @Override
        public void remove(AudioRecord audioRecord) throws Exception {
            File file = getUploadsPath().resolve(audioRecord.getFilename()).toFile();
            file.delete();

            audioRecordRepository.delete(audioRecord);
            audioRecordRepository.flush();
        }

}
