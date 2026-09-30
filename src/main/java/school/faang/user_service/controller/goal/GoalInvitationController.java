package school.faang.user_service.controller.goal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.goal.GoalInvitationDto;
import school.faang.user_service.service.goal.GoalInvitationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goal-invitation")
public class GoalInvitationController {
    private final GoalInvitationService invitationService;

    @PostMapping("/{goalId}/invite/{invitedId}")
    public GoalInvitationDto createGoalInvitation(@PathVariable Long goalId,
                                                  @PathVariable Long invitedId) {
        return invitationService.createGoalInvitation(goalId, invitedId);
    }

    @PostMapping("/{invitationId}/accept")
    public void acceptInvitation(@PathVariable Long invitationId) {
        invitationService.acceptInvitation(invitationId);
    }

    @PostMapping("/{invitationId}/reject")
    public void rejectInvitation(@PathVariable Long invitationId) {
        invitationService.rejectInvitation(invitationId);
    }
}
