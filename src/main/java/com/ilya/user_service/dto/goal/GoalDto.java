package com.ilya.user_service.dto.goal;

import lombok.Data;
import com.ilya.user_service.entity.goal.GoalStatus;

import java.time.LocalDateTime;

@Data
public class GoalDto {
    private String title;
    private String description;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private GoalStatus status;
    private Long mentorId;
}
