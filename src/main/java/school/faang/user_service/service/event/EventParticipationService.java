package school.faang.user_service.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.event.Event;
import school.faang.user_service.exceptions.BadRequestException;
import school.faang.user_service.exceptions.EntityNotFoundException;
import school.faang.user_service.mapper.user.UserMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.event.EventParticipationRepository;
import school.faang.user_service.repository.event.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventParticipationService {
    private final EventRepository eventRepository;
    private final EventParticipationRepository eventParticipationRepository;
    private final UserRepository userRepository;
    private final UserContext userContext;
    private final UserMapper userMapper;

    @Transactional
    public void registrationParticipant(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        int numberOfParticipation = eventParticipationRepository.countParticipants(event.getId());
        if (numberOfParticipation + 1 > event.getMaxAttendees()) {
            throw new BadRequestException("Не достаточно мест на это событие");
        }

        List<User> attendees = event.getAttendees();
        User participant = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        boolean isItOnTheList = attendees.stream()
                .anyMatch((user) -> user.getId().equals(participant.getId()));

        if (isItOnTheList) {
            throw new BadRequestException("Этот пользователь уже является участником события");
        }

        eventParticipationRepository.register(event.getId(), participant.getId());
        log.info("Пользователь зарегистрировался на событие: userId={}, eventId={}",
                participant.getId(), eventId);
    }

    @Transactional
    public void unregisterParticipant(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        List<User> attendees = event.getAttendees();
        User participant = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        boolean isItOnTheList = attendees.stream()
                .anyMatch((user) -> user.getId().equals(participant.getId()));

        if (!isItOnTheList) {
            throw new BadRequestException("Этот пользователь не является участником события");
        }

        eventParticipationRepository.unregister(event.getId(), participant.getId());
        log.info("Пользователь отменил участие в событии: userId={}, eventId={}",
                participant.getId(), eventId);
    }

    public int countParticipantsByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        return eventParticipationRepository.countParticipants(event.getId());
    }

    public List<UserDto> listParticipantsByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        List<User> participations = eventParticipationRepository.findAllParticipantsByEventId(event.getId());
        return userMapper.toUserDto(participations);
    }
}
