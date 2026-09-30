package school.faang.user_service.dto.goal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.goal.GoalStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateGoalDto {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private GoalStatus status;
    @NotNull
    private Long mentorId;
    private List<Long> usersIds;
}
