package com.etsia.interaction.domain.service;

import com.etsia.common.domain.model.NetworkStatsDto;
import com.etsia.common.domain.model.NetworkSuggestionDto;
import com.etsia.common.infrastructure.config.Mapper;
import com.etsia.interaction.infrastructure.repository.JpaChannelFollowerRepository;
import com.etsia.interaction.infrastructure.repository.JpaUserFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NetworkService {

    private final JpaUserFollowRepository userFollowRepository;
    private final JpaChannelFollowerRepository channelFollowerRepository;

    public NetworkStatsDto getStats(Integer userId) {
        long connections = userFollowRepository.countByFollowerId(userId);
        long channels = channelFollowerRepository.countByUserId(userId);
        // Contacts are typically imported from the phone, for now we can mock it or use connections
        long contacts = connections * 2 + 10; // Mocked for now to match UI vibes

        return NetworkStatsDto.builder()
                .connectionsCount(connections)
                .channelsCount(channels)
                .contactsCount(contacts)
                .build();
    }

    public List<NetworkSuggestionDto> getSuggestions(Integer userId) {
        return userFollowRepository.findSuggestions(userId, PageRequest.of(0, 10))
                .stream()
                .map(user -> NetworkSuggestionDto.builder()
                        .user(Mapper.toUserDto(user))
                        .mutualConnectionsCount(userFollowRepository.countMutualFollows(userId, user.getId()))
                        .reason("Suggested for you")
                        .build())
                .collect(Collectors.toList());
    }
}
