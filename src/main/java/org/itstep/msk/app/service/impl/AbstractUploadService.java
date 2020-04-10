package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.Upload;
import org.itstep.msk.app.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public abstract class AbstractUploadService<T extends Upload> implements UploadService<T> {

    private String uploadsPath;

    public AbstractUploadService(@Value("${upload.path}") String uploadsPath) {
        this.uploadsPath = uploadsPath;
    }

    @Override
    public Path getAbsolutePath(T upload) {
        return getUploadsPath().resolve(upload.getFilename());
    }

    protected Path getUploadsPath() {
        return Paths.get(uploadsPath).toAbsolutePath();
    }

    protected String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + getFileExtension(originalFilename);
    }

    protected String getFileExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index >= 0) {
            return filename.substring(index);
        }

        return "";
    }
}