package com.ilya.user_service.controller.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ilya.user_service.dto.user.UserDto;
import com.ilya.user_service.service.event.EventParticipationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event-participation")
public class EventParticipationController {
    private final EventParticipationService participationService;

    @PostMapping("/{eventId}")
    public void registrationParticipant(@PathVariable Long eventId) {
        participationService.registrationParticipant(eventId);
    }

    @DeleteMapping("/{eventId}")
    public void unregisterParticipant(@PathVariable Long eventId) {
        participationService.unregisterParticipant(eventId);
    }

    @GetMapping("/{eventId}/count")
    public int countParticipantsByEventId(@PathVariable Long eventId) {
        return participationService.countParticipantsByEventId(eventId);
    }

    @GetMapping("/{eventId}/participants")
    public List<UserDto> listParticipantsByEventId(@PathVariable Long eventId) {
        return participationService.listParticipantsByEventId(eventId);
    }
}
