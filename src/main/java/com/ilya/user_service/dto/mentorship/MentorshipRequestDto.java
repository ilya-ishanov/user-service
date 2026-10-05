package com.ilya.user_service.dto.mentorship;

import lombok.Data;
import com.ilya.user_service.dto.user.UserDto;

@Data
public class MentorshipRequestDto {
    private Long id;
    private String description;
    private UserDto requester;
    private UserDto receiver;
}
