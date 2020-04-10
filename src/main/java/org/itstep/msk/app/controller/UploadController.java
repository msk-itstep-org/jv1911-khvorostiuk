package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.exceptions.HttpNotFoundException;
import org.itstep.msk.app.entity.Avatar;
import org.itstep.msk.app.service.impl.AudioRecordServiceImpl;
import org.itstep.msk.app.service.impl.AvatarServiceImpl;
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
    private AvatarServiceImpl avatarService;

    @Autowired
    private AudioRecordServiceImpl audioRecordService;

    @GetMapping("/avatar/{avatarId}")
    @ResponseBody
    public ResponseEntity<Resource> avatar(@PathVariable("avatarId") Avatar avatar) throws MalformedURLException {
        Resource file = new UrlResource(avatarService.getAbsolutePath(avatar).toUri());

        if (!file.exists() || !file.isReadable()) {
            throw new HttpNotFoundException();
        }

        return ResponseEntity.ok().body(file);
    }

    @GetMapping("/audio/{audioId}")
    @ResponseBody
    public ResponseEntity<Resource> audioRecord(@PathVariable("audioId") AudioRecord audioRecord) throws MalformedURLException {
        Resource file = new UrlResource(audioRecordService.getAbsolutePath(audioRecord).toUri());

        if (!file.exists() || !file.isReadable()) {
            throw new HttpNotFoundException();
        }

        return ResponseEntity.ok().body(file);
    }

}
