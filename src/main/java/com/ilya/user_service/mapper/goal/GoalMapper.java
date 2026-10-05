package com.ilya.user_service.mapper.goal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.ilya.user_service.dto.goal.CreateGoalDto;
import com.ilya.user_service.dto.goal.GoalDto;
import com.ilya.user_service.dto.goal.UpdateGoalDto;
import com.ilya.user_service.entity.goal.Goal;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoalMapper {

    @Mapping(target = "mentor", ignore = true)
    @Mapping(target = "users", ignore = true)
    Goal toGoalEntity(CreateGoalDto dto);

    GoalDto toGoalDto(Goal goal);

    List<GoalDto> toListGoalDto(List<Goal> goals);

    void updateGoal(UpdateGoalDto dto, @MappingTarget Goal entity);
}
