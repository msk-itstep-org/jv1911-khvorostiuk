package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.exceptions.NotFoundException;
import org.itstep.msk.app.entity.Image;
import org.itstep.msk.app.service.impl.AudioRecordUploadServiceImpl;
import org.itstep.msk.app.service.impl.AvatarServiceUploadImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Controller
@RequestMapping("/uploads")
public class UploadController {
    @Autowired
    private AvatarServiceUploadImpl avatarService;

    @Autowired
    private AudioRecordUploadServiceImpl audioRecordService;

    @GetMapping("/avatar/{avatarId}")
    @ResponseBody
    public ResponseEntity<Resource> avatar(@PathVariable("avatarId") Image avatar) throws MalformedURLException {
        Resource file = new UrlResource(avatarService.getAbsolutePath(avatar).toUri());

        if (!file.exists() || !file.isReadable()) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok().body(file);
    }

    @GetMapping("/audio/{audioId}")
    @ResponseBody
    public ResponseEntity<Resource> audioRecord(@PathVariable("audioId") AudioRecord audioRecord) throws MalformedURLException {
        Resource file = new UrlResource(audioRecordService.getAbsolutePath(audioRecord).toUri());

        if (!file.exists() || !file.isReadable()) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok().body(file);
    }

    @GetMapping("/image/{imageId}")
    @ResponseBody
    public ResponseEntity<Resource> image(@PathVariable("imageId") Image image) throws MalformedURLException {
        Resource file = new UrlResource(avatarService.getAbsolutePath(image).toUri());

        if (!file.exists() || !file.isReadable()) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok().body(file);
    }

}
