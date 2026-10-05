package com.ilya.user_service.dto.workschedule;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateWorkScheduleDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime startLunch;
    private LocalTime endLunch;
    @NotBlank
    private String timezone;
}
