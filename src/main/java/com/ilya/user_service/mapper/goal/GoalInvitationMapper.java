package com.ilya.user_service.mapper.goal;

import org.mapstruct.Mapper;
import com.ilya.user_service.dto.goal.GoalInvitationDto;
import com.ilya.user_service.entity.goal.GoalInvitation;

@Mapper(componentModel = "spring")
public interface GoalInvitationMapper {

    GoalInvitationDto toGoalInvitationDto(GoalInvitation goalInvitation);
}
