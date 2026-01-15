package com.etsia.interaction.domain.repository;

public interface FollowRepository {

    void follow(int followerId, int followedId);

    void unfollow(Integer followerId, Integer followedId);

    Boolean isFollowing(Integer followerId, Integer followedId);

    java.util.List<Integer> getFollowedUserIds(Integer followerId);
}
