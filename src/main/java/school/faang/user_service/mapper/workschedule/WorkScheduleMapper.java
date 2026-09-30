package school.faang.user_service.mapper.workschedule;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import school.faang.user_service.dto.workschedule.CreateWorkScheduleDto;
import school.faang.user_service.dto.workschedule.UpdateWorkScheduleDto;
import school.faang.user_service.dto.workschedule.WorkScheduleDto;
import school.faang.user_service.entity.WorkSchedule;

@Mapper(componentModel = "spring")
public interface WorkScheduleMapper {
    WorkScheduleDto toWorkScheduleDto(WorkSchedule workSchedule);
    WorkSchedule toWorkScheduleEntity(CreateWorkScheduleDto workScheduleDto);

    void updateWorkSchedule(UpdateWorkScheduleDto dto, @MappingTarget WorkSchedule workSchedule);
}
