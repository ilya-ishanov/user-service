package school.faang.user_service.service.mentorship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.dto.RejectionDto;
import school.faang.user_service.dto.mentorship.CreateMentorshipRequestDto;
import school.faang.user_service.dto.mentorship.MentorshipRequestDto;
import school.faang.user_service.entity.Mentorship;
import school.faang.user_service.entity.MentorshipRequest;
import school.faang.user_service.entity.RequestStatus;
import school.faang.user_service.entity.User;
import school.faang.user_service.exceptions.BadRequestException;
import school.faang.user_service.exceptions.EntityNotFoundException;
import school.faang.user_service.mapper.mentorship.MentorshipRequestMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.mentorship.MentorshipRepository;
import school.faang.user_service.repository.mentorship.MentorshipRequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentorshipRequestService {
    private final MentorshipRequestRepository mentorshipRequestRepository;
    private final UserContext userContext;
    private final MentorshipRequestMapper mapper;
    private final UserRepository userRepository;
    private final MentorshipRepository mentorshipRepository;
    private final MentorshipService mentorshipService;

    public MentorshipRequestDto createMentorshipRequest(CreateMentorshipRequestDto dto) {
        User mentee = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        User mentor = userRepository.findUserById(dto.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("User", dto.getReceiverId()));

        if (mentee.getId().equals(mentor.getId())) {
            throw new BadRequestException("пользователь не может отправить запрос самому себе");
        }

        Optional<MentorshipRequest> mentorshipRequest = mentorshipRequestRepository
                .findLatestRequest(mentee.getId(), mentor.getId());

        if (mentorshipRequest.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdTime = mentorshipRequest.get().getCreatedAt();

            if (now.isBefore(createdTime.plusMonths(3))) {
                throw new BadRequestException("запрос на менторство можно делать раз в 3 месяца");
            }
        }

        MentorshipRequest request = MentorshipRequest.builder()
                .requester(mentee)
                .receiver(mentor)
                .description(dto.getDescription())
                .status(RequestStatus.PENDING)
                .build();

        request = mentorshipRequestRepository.save(request);
        log.info("Создан запрос на менторство: requestId={}, menteeId={}, mentorId={}",
                request.getId(), mentee.getId(), mentor.getId());
        return mapper.toMentorshipRequestDto(request);
    }

    @Transactional
    public void acceptRequestMentorship(Long requestId) {
        MentorshipRequest mentorshipRequest = mentorshipRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("MentorshipRequest", requestId));

        if (mentorshipRequest.getStatus().equals(RequestStatus.ACCEPTED) ||
                mentorshipRequest.getStatus().equals(RequestStatus.REJECTED)) {
            throw new BadRequestException("Ментор не может принять запрос от пользователя, если он уже принят или отклонен");
        }

        User receiver = mentorshipRequest.getReceiver();
        if (!receiver.getId().equals(userContext.getUserId())) {
            throw new BadRequestException("Принять запрос на менторство может только тот пользователь, которому оно адресовано");
        }

        mentorshipService.addMentorship(receiver.getId(), mentorshipRequest.getRequester().getId());

        mentorshipRequest.setStatus(RequestStatus.ACCEPTED);
        mentorshipRequestRepository.save(mentorshipRequest);
        log.info("Запрос на менторство принят: requestId={}, mentorId={}, menteeId={}",
                requestId,
                receiver.getId(),
                mentorshipRequest.getRequester().getId());
    }

    public void rejectMentorshipRequest(Long requestId, RejectionDto rejectionDto) {
        MentorshipRequest mentorshipRequest = mentorshipRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("MentorshipRequest", requestId));

        User receiver = mentorshipRequest.getReceiver();
        if (!receiver.getId().equals(userContext.getUserId())) {
            throw new BadRequestException("Отклонить запрос на менторство может только тот пользователь, которому оно адресовано");
        }

        if (!mentorshipRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new BadRequestException("Ментор не может принять запрос от пользователя, если он уже принят или отклонен");
        }

        mentorshipRequest.setStatus(RequestStatus.REJECTED);
        mentorshipRequest.setRejectionReason(rejectionDto.reason);
        mentorshipRequestRepository.save(mentorshipRequest);
        log.info("Запрос на менторство отклонен: requestId={}, userId={}",
                requestId, receiver.getId());
    }

    public List<MentorshipRequestDto> getAllByRequesterId(Long requesterId) {
         return mapper.toListMentorshipRequestDto(mentorshipRequestRepository.getAllByRequesterId(requesterId));
    }

    public List<MentorshipRequestDto> getAllByReceiverId(Long receiverId) {
        return mapper.toListMentorshipRequestDto(mentorshipRequestRepository.getAllByReceiverId(receiverId));
    }
}


