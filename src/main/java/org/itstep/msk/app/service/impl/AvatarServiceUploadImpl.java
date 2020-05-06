package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.Image;
import org.itstep.msk.app.exceptions.UnsupportedMediaTypeException;
import org.itstep.msk.app.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

@Service
public class AvatarServiceUploadImpl extends AbstractUploadService<Image> {
    final private String[] CONTENT_TYPES = {"image/png", "image/jpeg"};
    private AvatarRepository avatarRepository;

    public AvatarServiceUploadImpl(@Autowired AvatarRepository avatarRepository,
                                   @Value("${upload.path}") String uploadsPath) {
        super(uploadsPath);
        this.avatarRepository = avatarRepository;
    }

    @Override
    public Image upload(MultipartFile file) throws Exception {
        String originalFileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
        String filename = generateUniqueFileName(originalFileName);

        if (!Arrays.asList(CONTENT_TYPES).contains(file.getContentType())) {
            throw new UnsupportedMediaTypeException();
        }

        file.transferTo(getUploadsPath().resolve(filename).toFile());

        Image avatar = new Image();
        avatar.setFilename(filename);
        avatar.setOriginalFilename(originalFileName);
        avatar.setContentType(file.getContentType());

        avatarRepository.save(avatar);
        avatarRepository.flush();

        return avatar;
    }

    @Override
    public void remove(Image avatar) throws Exception {
        File file = getUploadsPath().resolve(avatar.getFilename()).toFile();
        file.delete();

        avatarRepository.delete(avatar);
        avatarRepository.flush();
    }

}
