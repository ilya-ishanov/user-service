package school.faang.user_service.controller.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.event.CreateEventDto;
import school.faang.user_service.dto.event.EventDto;
import school.faang.user_service.dto.event.UpdateEventDto;
import school.faang.user_service.service.event.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @PostMapping
    public EventDto createEvent(@Valid @RequestBody CreateEventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @PutMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable Long eventId,
                                @Valid @RequestBody UpdateEventDto updateEventDto) {
        return eventService.updateEvent(eventId, updateEventDto);
    }

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
    }

    @GetMapping("/{userId}")
    public List<EventDto> getEvents(@PathVariable Long userId) {
        return eventService.getEvents(userId);
    }
}
