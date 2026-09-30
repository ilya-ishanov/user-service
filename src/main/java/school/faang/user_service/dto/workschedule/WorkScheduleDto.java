package school.faang.user_service.dto.workschedule;

import lombok.Data;

import java.time.LocalTime;

@Data
public class WorkScheduleDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime startLunch;
    private LocalTime endLunch;
    private String timezone;
}
