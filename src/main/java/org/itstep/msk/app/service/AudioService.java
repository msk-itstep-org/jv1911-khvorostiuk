package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.AudioRecord;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.exceptions.UnsupportedContentTypeException;

public interface AudioService {
    void add(User user, AudioRecord audioRecord) throws UnsupportedContentTypeException;

    String getAudioStatus(User user, AudioRecord audioRecord);
}
