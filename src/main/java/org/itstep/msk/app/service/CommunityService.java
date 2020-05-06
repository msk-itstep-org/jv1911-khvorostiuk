package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.Community;
import org.itstep.msk.app.entity.User;

public interface CommunityService {
    void create(Community community, User user);

    void join(User user, Community community);

    void leave(User member, Community community);

    void edit(Community community, Community editedCommunity);
}
