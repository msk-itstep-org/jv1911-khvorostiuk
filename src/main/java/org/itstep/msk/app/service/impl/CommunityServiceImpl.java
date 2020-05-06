package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.Community;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.repository.CommunityRepository;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;

    private final UserRepository userRepository;

    @Autowired
    public CommunityServiceImpl(CommunityRepository communityRepository, UserRepository userRepository) {
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void create(Community community, User user) {
        user.setCommunityMember(true);
        community.setOwner(user);

        userRepository.save(user);
        userRepository.flush();
        communityRepository.save(community);
        communityRepository.flush();
    }

    @Override
    public void join(User user, Community community) {
        community.getMembers().add(user);

        communityRepository.save(community);
        communityRepository.flush();
    }

    @Override
    public void leave(User member, Community community) {
        community.getMembers().remove(member);

        communityRepository.save(community);
        communityRepository.flush();
    }

    @Override
    public void edit(Community community, Community editedCommunity) {
        community.setOwner(editedCommunity.getOwner());
        community.setAvatar(editedCommunity.getAvatar());
        community.setMembers(editedCommunity.getMembers());
        community.setName(editedCommunity.getName());

        communityRepository.save(community);
        communityRepository.flush();
    }
}
