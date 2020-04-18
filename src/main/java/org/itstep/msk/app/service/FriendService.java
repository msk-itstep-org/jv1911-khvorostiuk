package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.User;

public interface FriendService {
    void add(User currentUser, User user);

    void remove(User currentUser, User user);

    String getFriendStatus(User currentUser, User user);

}
