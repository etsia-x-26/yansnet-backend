package com.etsia.interaction.domain.service;

import com.etsia.interaction.domain.repository.FollowRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class FollowDomainService {

    private final FollowRepository followRepository;

    public FollowDomainService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public Boolean isFollowing(Integer followerId, Integer followedId) {
        return followRepository.isFollowing(followerId, followedId);
    }

    public java.util.List<Integer> getFollowedUserIds(Integer followerId) {
        return followRepository.getFollowedUserIds(followerId);
    }
}
