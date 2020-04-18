package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.Upload;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Set;


public interface UploadService<T extends Upload> {
    T upload(MultipartFile file) throws Exception;

    void remove(T upload) throws Exception;

    Path getAbsolutePath(T upload);

}
