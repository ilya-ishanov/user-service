package school.faang.user_service.dto.goal;

import lombok.Data;
import school.faang.user_service.entity.goal.GoalStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GoalDto {
    private String title;
    private String description;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private GoalStatus status;
    private Long mentorId;
}
