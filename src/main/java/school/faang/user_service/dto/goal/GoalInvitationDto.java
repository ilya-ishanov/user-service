package school.faang.user_service.dto.goal;

import lombok.Data;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.entity.RequestStatus;

@Data
public class GoalInvitationDto {
    private Long id;
    private UserDto inviter;
    private UserDto invited;
    private RequestStatus status;
}
