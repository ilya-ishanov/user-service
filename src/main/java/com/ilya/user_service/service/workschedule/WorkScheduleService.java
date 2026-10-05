package com.ilya.user_service.service.workschedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.ilya.user_service.config.context.UserContext;
import com.ilya.user_service.dto.workschedule.CreateWorkScheduleDto;
import com.ilya.user_service.dto.workschedule.UpdateWorkScheduleDto;
import com.ilya.user_service.dto.workschedule.WorkScheduleDto;
import com.ilya.user_service.entity.User;
import com.ilya.user_service.entity.WorkSchedule;
import com.ilya.user_service.exceptions.BadRequestException;
import com.ilya.user_service.exceptions.EntityNotFoundException;
import com.ilya.user_service.mapper.workschedule.WorkScheduleMapper;
import com.ilya.user_service.repository.UserRepository;
import com.ilya.user_service.repository.WorkScheduleRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkScheduleService {
    private final WorkScheduleRepository scheduleRepository;
    private final UserContext userContext;
    private final WorkScheduleMapper mapper;
    private final UserRepository userRepository;

    @Transactional
    public WorkScheduleDto createWorkSchedule(CreateWorkScheduleDto scheduleDto) {
        LocalTime startTime = scheduleDto.getStartTime();
        LocalTime endTime = scheduleDto.getEndTime();
        LocalTime startLunch = scheduleDto.getStartLunch();
        LocalTime endLunch = scheduleDto.getEndLunch();
        validateWorkSchedule(startTime, endTime, startLunch, endLunch);

        User user = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));
        WorkSchedule workSchedule = mapper.toWorkScheduleEntity(scheduleDto);
        workSchedule.setUser(user);
        scheduleRepository.save(workSchedule);
        log.info("Создан рабочий график: scheduleId={}, userId={}",
                workSchedule.getId(), user.getId());
        return mapper.toWorkScheduleDto(workSchedule);
    }

    @Transactional
    public WorkScheduleDto updateWorkSchedule(Long scheduleId, UpdateWorkScheduleDto scheduleDto) {
        LocalTime startTime = scheduleDto.getStartTime();
        LocalTime endTime = scheduleDto.getEndTime();
        LocalTime startLunch = scheduleDto.getStartLunch();
        LocalTime endLunch = scheduleDto.getEndLunch();
        validateWorkSchedule(startTime, endTime, startLunch, endLunch);

        User user = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));
        WorkSchedule workSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("WorkSchedule", scheduleId));

        if (!workSchedule.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Обновлять можно только свои данные");
        }

        mapper.updateWorkSchedule(scheduleDto, workSchedule);
        scheduleRepository.save(workSchedule);
        log.info("Обновлен рабочий график: scheduleId={}, userId={}",
                scheduleId, user.getId());
        return mapper.toWorkScheduleDto(workSchedule);
    }

    private void validateWorkSchedule(LocalTime startTime, LocalTime endTime,
    LocalTime startLunch, LocalTime endLunch) {

        if (!startTime.isBefore(endTime)) {
            throw new BadRequestException("Начало рабочего дня должно быть раньше окончания рабочего дня");
        }

        if (!startLunch.isBefore(endLunch)) {
            throw new BadRequestException("Начало обеда должно быть раньше окончания обеда");
        }

        if (!startTime.isBefore(startLunch) || !endTime.isAfter(endLunch)) {
            throw new BadRequestException("Обед должен быть внутри рабочего дня");
        }
    }

    public WorkScheduleDto getWorkScheduleById(Long scheduleId) {
        WorkSchedule workSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("WorkSchedule", scheduleId));
        return mapper.toWorkScheduleDto(workSchedule);
    }
}
