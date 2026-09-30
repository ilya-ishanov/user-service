package school.faang.user_service.mapper.mentorship;

import org.mapstruct.Mapper;
import school.faang.user_service.dto.mentorship.MentorshipRequestDto;
import school.faang.user_service.entity.MentorshipRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MentorshipRequestMapper {
    MentorshipRequestDto toMentorshipRequestDto(MentorshipRequest mentorshipRequest);

    List<MentorshipRequestDto> toListMentorshipRequestDto(List<MentorshipRequest> mentorshipRequest);
}
