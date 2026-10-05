package com.ilya.user_service.service.subscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ilya.user_service.config.context.UserContext;
import com.ilya.user_service.dto.user.UserDto;
import com.ilya.user_service.entity.User;
import com.ilya.user_service.exceptions.BadRequestException;
import com.ilya.user_service.exceptions.EntityNotFoundException;
import com.ilya.user_service.mapper.user.UserMapper;
import com.ilya.user_service.repository.SubscriptionRepository;
import com.ilya.user_service.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserContext userContext;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public void followUser(Long followeeId) {
        User follower = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        User followee = userRepository.findUserById(followeeId)
                .orElseThrow(() -> new EntityNotFoundException("User",followeeId));

        if (follower.getId().equals(followee.getId())) {
            throw new BadRequestException("Пользователь не может подписаться сам на себя");
        }

        if (subscriptionRepository.existsByFollowerIdAndFolloweeId(follower.getId(), followee.getId())) {
            throw new BadRequestException("Такая связь уже существует");
        }
        subscriptionRepository.followUser(follower.getId(), followee.getId());
        log.info("Пользователь подписался: followerId={}, followeeId={}",
                follower.getId(), followeeId);
    }

    @Transactional
    public void unfollowUser(Long followeeId) {
        User follower = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User",userContext.getUserId()));

        User followee = userRepository.findUserById(followeeId)
                .orElseThrow(() -> new EntityNotFoundException("User",followeeId));

        if (follower.getId().equals(followee.getId())) {
            throw new BadRequestException("Пользователь не может отписаться от самого себя");
        }

        if (!subscriptionRepository.existsByFollowerIdAndFolloweeId(follower.getId(), followee.getId())) {
            throw new BadRequestException("отписка невозможна, потому что подписки не существует.");
        }
        subscriptionRepository.unfollowUser(follower.getId(), followee.getId());
        log.info("Пользователь отписался: followerId={}, followeeId={}",
                follower.getId(), followeeId);
    }

    public int getFollowersCount(Long followeeId) {
        User followee = userRepository.findUserById(followeeId)
                .orElseThrow(() -> new EntityNotFoundException("User",followeeId));
        return subscriptionRepository.findFollowersAmountByFolloweeId(followee.getId());
    }

    public int getFolloweeCount(Long followerId) {
        User follower = userRepository.findUserById(followerId)
                .orElseThrow(() -> new EntityNotFoundException("User",followerId));
        return subscriptionRepository.findFolloweesAmountByFollowerId(follower.getId());
    }

    public List<UserDto> getFollowers(Long followeeId) {
        User followee = userRepository.findUserById(followeeId)
                .orElseThrow(() -> new EntityNotFoundException("User",followeeId));
        List<User> followers = subscriptionRepository.findByFolloweeId(followee.getId());
        return userMapper.toUserDto(followers);
    }

    public List<UserDto> getFollowees(Long followerId) {
        User follower = userRepository.findUserById(followerId)
                .orElseThrow(() -> new EntityNotFoundException("User",followerId));
        List<User> followees = subscriptionRepository.findByFollowerId(follower.getId());
        return userMapper.toUserDto(followees);
    }
}
