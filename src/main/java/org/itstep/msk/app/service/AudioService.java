package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.entity.Post;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.UnsupportedMediaTypeException;

public interface AudioService {
    void add(User user, AudioRecord audioRecord) throws UnsupportedMediaTypeException;

    void addToPost(Post post, AudioRecord audioRecord);

    String getAudioStatus(User user, AudioRecord audioRecord);
}
