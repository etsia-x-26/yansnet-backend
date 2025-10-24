package com.etsia.interaction.application.service;

import com.etsia.interaction.domain.repository.ChannelFollowRepository;
import com.etsia.interaction.domain.service.ChannelFollowDomainService;
import org.springframework.stereotype.Service;

@Service
public class UnFollowChannelUseCase {
    private final ChannelFollowDomainService channelFollowDomainService;
    private final ChannelFollowRepository channelFollowRepository;

    public UnFollowChannelUseCase(ChannelFollowDomainService channelFollowDomainService, ChannelFollowRepository channelFollowRepository) {
        this.channelFollowDomainService = channelFollowDomainService;
        this.channelFollowRepository = channelFollowRepository;
    }

    public void execute(Integer followerId, Integer followedId) {
        if (!channelFollowDomainService.isFollowing(followerId, followedId)) {
            throw new IllegalArgumentException("Follow does not exist");
        }
        channelFollowRepository.unfollow(followerId, followedId);
    }
}
