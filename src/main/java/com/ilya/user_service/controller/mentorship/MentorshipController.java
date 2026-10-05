package com.ilya.user_service.controller.mentorship;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ilya.user_service.dto.user.UserDto;
import com.ilya.user_service.service.mentorship.MentorshipService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mentorship")
public class MentorshipController {
    private final MentorshipService mentorshipService;

    @PostMapping("/mentor/{mentorId}/mentee/{menteeId}")
    public void addMentorship(@PathVariable Long mentorId, @PathVariable Long menteeId) {
        mentorshipService.addMentorship(mentorId, menteeId);
    }

    @GetMapping("/mentor/{mentorId}/mentees")
    List<UserDto> getMentees(@PathVariable Long mentorId) {
        return mentorshipService.getMentees(mentorId);
    }

    @GetMapping("/mentee/{menteeId}/mentors")
    public List<UserDto> getMentors(@PathVariable Long menteeId) {
        return mentorshipService.getMentors(menteeId);
    }

    @DeleteMapping("/mentee/{menteeId}/mentor/{mentorId}")
    public void delete(@PathVariable Long menteeId, @PathVariable Long mentorId) {
        mentorshipService.delete(menteeId, mentorId);
    }
}
