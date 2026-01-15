package com.etsia.interaction.infrastructure.adapters;

import com.etsia.common.infrastructure.entities.User;
import com.etsia.common.infrastructure.entities.UserFollow;
import com.etsia.common.infrastructure.entities.UserFollowId;
import com.etsia.interaction.domain.repository.FollowRepository;
import com.etsia.interaction.infrastructure.repository.JpaUserFollowRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserFollowRepositoryImpl implements FollowRepository {

    private final JpaUserFollowRepository jpaUserFollowRepository;

    public UserFollowRepositoryImpl(JpaUserFollowRepository jpaUserFollowRepository) {
        this.jpaUserFollowRepository = jpaUserFollowRepository;
    }

    @Override
    public void follow(int followerId, int followedId) {
        try {
            jpaUserFollowRepository.save(UserFollow.builder()
                            .id(new UserFollowId(followerId, followedId))
                    .followed(User.builder().id(followedId).build())
                    .follower(User.builder().id(followerId).build())
                    .build());
        } catch (Exception e) {
            System.out.println(e);
            throw new IllegalArgumentException("Follow already exists or user does not exist");
        }

    }

    @Override
    public void unfollow(Integer followerId, Integer followedId) {
        try {
            jpaUserFollowRepository.deleteById(new UserFollowId(followerId, followedId));
        } catch (Exception e) {
            throw new IllegalArgumentException("Follow does not exist");
        }

    }

    @Override
    public Boolean isFollowing(Integer followerId, Integer followedId) {
        return jpaUserFollowRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Override
    public java.util.List<Integer> getFollowedUserIds(Integer followerId) {
        return jpaUserFollowRepository.findFollowedUserIds(followerId);
    }
}
