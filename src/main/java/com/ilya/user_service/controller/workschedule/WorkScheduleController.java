package com.ilya.user_service.controller.workschedule;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ilya.user_service.dto.workschedule.CreateWorkScheduleDto;
import com.ilya.user_service.dto.workschedule.UpdateWorkScheduleDto;
import com.ilya.user_service.dto.workschedule.WorkScheduleDto;
import com.ilya.user_service.service.workschedule.WorkScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/work-schedule")
public class WorkScheduleController {
    private final WorkScheduleService scheduleService;

    @PostMapping
    public WorkScheduleDto createWorkSchedule(@Valid @RequestBody CreateWorkScheduleDto scheduleDto) {
        return scheduleService.createWorkSchedule(scheduleDto);
    }

    @PutMapping("/{scheduleId}")
    public WorkScheduleDto updateWorkSchedule(@PathVariable Long scheduleId,
                                              @Valid @RequestBody UpdateWorkScheduleDto scheduleDto) {
        return scheduleService.updateWorkSchedule(scheduleId, scheduleDto);
    }

    @GetMapping("/{scheduleId}")
    public WorkScheduleDto getWorkScheduleById(@PathVariable Long scheduleId) {
        return scheduleService.getWorkScheduleById(scheduleId);
    }
}
