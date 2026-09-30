package school.faang.user_service.dto.event;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import school.faang.user_service.entity.event.EventStatus;
import school.faang.user_service.entity.event.EventType;

import java.time.LocalDateTime;

@Data
public class UpdateEventDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @NotBlank
    private String location;
    private int maxAttendees;
    private EventType type;
    private EventStatus status;
}
