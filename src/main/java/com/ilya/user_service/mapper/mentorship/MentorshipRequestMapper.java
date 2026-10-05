package com.ilya.user_service.mapper.mentorship;

import org.mapstruct.Mapper;
import com.ilya.user_service.dto.mentorship.MentorshipRequestDto;
import com.ilya.user_service.entity.MentorshipRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MentorshipRequestMapper {
    MentorshipRequestDto toMentorshipRequestDto(MentorshipRequest mentorshipRequest);

    List<MentorshipRequestDto> toListMentorshipRequestDto(List<MentorshipRequest> mentorshipRequest);
}
