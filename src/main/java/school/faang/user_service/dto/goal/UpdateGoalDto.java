package school.faang.user_service.dto.goal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import school.faang.user_service.entity.goal.GoalStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateGoalDto {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private LocalDateTime deadline;
    private GoalStatus status;
    @NotNull
    private Long mentorId;
}
