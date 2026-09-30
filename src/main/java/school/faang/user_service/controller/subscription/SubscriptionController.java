package school.faang.user_service.controller.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.service.subscription.SubscriptionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/{followeeId}/follow")
    public void followUser(@PathVariable Long followeeId) {
        subscriptionService.followUser(followeeId);
    }

    @DeleteMapping("/{followeeId}/unfollow")
    public void unfollowUser(@PathVariable Long followeeId) {
        subscriptionService.unfollowUser(followeeId);
    }

    @GetMapping("/{followeeId}/followers/count/")
    public int getFollowersCount(@PathVariable Long followeeId) {
        return subscriptionService.getFollowersCount(followeeId);
    }

    @GetMapping("/{followerId}/followees/count")
    public int getFolloweeCount(@PathVariable Long followerId) {
        return subscriptionService.getFolloweeCount(followerId);
    }

    @GetMapping("/{followeeId}/followers")
    public List<UserDto> getFollowers(@PathVariable Long followeeId) {
            return subscriptionService.getFollowers(followeeId);
    }

    @GetMapping("/{followerId}/followees")
    public List<UserDto> getFollowees(@PathVariable Long followerId) {
        return subscriptionService.getFollowees(followerId);
    }
}
