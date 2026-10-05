package com.ilya.user_service.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.ilya.user_service.config.context.UserContext;
import com.ilya.user_service.dto.event.CreateEventDto;
import com.ilya.user_service.dto.event.EventDto;
import com.ilya.user_service.dto.event.UpdateEventDto;
import com.ilya.user_service.entity.User;
import com.ilya.user_service.entity.event.Event;
import com.ilya.user_service.exceptions.BadRequestException;
import com.ilya.user_service.exceptions.EntityNotFoundException;
import com.ilya.user_service.mapper.event.EventMapper;
import com.ilya.user_service.repository.UserRepository;
import com.ilya.user_service.repository.event.EventRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final UserContext userContext;

    @Transactional
    public EventDto createEvent(CreateEventDto eventDto) {
        User owner = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        Event event = eventMapper.toEntityEvent(eventDto);
        event.setOwner(owner);
        Event savedEvent = eventRepository.save(event);
        log.info("Создано событие: eventId={}, ownerId={}",
                savedEvent.getId(), owner.getId());
        return eventMapper.toEventDto(savedEvent);
    }

    @Transactional
    public EventDto updateEvent(Long eventId, UpdateEventDto updateEventDto) {
        User owner = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        if (!event.getOwner().getId().equals(owner.getId())) {
            throw new BadRequestException("Обновить событие может только владелец");
        }

        eventMapper.toEntityUpdateEvent(updateEventDto, event);
        Event eventSaved = eventRepository.save(event);
        log.info("Обновлено событие: eventId={}, ownerId={}", eventId, owner.getId());
        return eventMapper.toEventDto(eventSaved);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        User owner = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        if (!event.getOwner().getId().equals(owner.getId())) {
            throw new BadRequestException("Удалить событие может только владелец");
        }

        eventRepository.delete(event);
        log.info("Удалено событие: eventId={}, ownerId={}",
                eventId, owner.getId());
    }

    public List<EventDto> getEvents(Long userId) {
        List<Event> events = eventRepository.findAllByUserId(userId);
        if (events.isEmpty()) {
            throw new BadRequestException("Список эвентов у userId - " + userId + " пустой");
        }

        return eventMapper.toListEventDto(events);
    }
}
