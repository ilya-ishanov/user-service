package school.faang.user_service.service.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.dto.recommendationrequest.CreateRecommendationRequestDto;
import school.faang.user_service.dto.recommendationrequest.RecommendationRequestDto;
import school.faang.user_service.dto.RejectionDto;
import school.faang.user_service.entity.RequestStatus;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.exceptions.BadRequestException;
import school.faang.user_service.exceptions.EntityNotFoundException;
import school.faang.user_service.mapper.recommendation.RecommendationRequestMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.recommendation.RecommendationRequestRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationRequestService {
    private final RecommendationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final UserContext userContext;
    private final RecommendationRequestMapper recommendationMapper;

    public RecommendationRequestDto createRecRequest(CreateRecommendationRequestDto requestDto) {
        User userReceiver = userRepository.findById(requestDto.getReceiver_id()).orElseThrow(
                () -> new EntityNotFoundException("User", requestDto.getReceiver_id())
        );

        User userRequester = userRepository.findById(userContext.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("User", userContext.getUserId())
        );

        if (userReceiver.getId().equals(userRequester.getId())) {
            throw new BadRequestException("Пользователь не может запросить рекомендацию у самого себя");
        }

        Optional<RecommendationRequest> request = requestRepository
                .findFirstByRequesterIdAndReceiverIdOrderByCreatedAtDesc
                        (userRequester.getId(), userReceiver.getId());

        RecommendationRequest finalRequest;
        if (request.isEmpty()) {
            finalRequest = createEntity(requestDto, userRequester, userReceiver);
        } else {
            RecommendationRequest oldRequest = request.get();
            LocalDateTime createdTime = oldRequest.getCreatedAt();
            LocalDateTime newDate = createdTime.plusMonths(6);
            LocalDateTime timeNow = LocalDateTime.now();

            if (timeNow.isAfter(newDate) || timeNow.isEqual(newDate)) {
                finalRequest = createEntity(requestDto, userRequester, userReceiver);
            } else {
                throw new BadRequestException("Прошло меньше шести месяцев, вы не можете сделать запрос");
            }
        }
        finalRequest = requestRepository.save(finalRequest);
        log.info("Создан запрос на рекомендацию: requestId={}, requesterId={}, receiverId={}",
                finalRequest.getId(), userRequester.getId(), userReceiver.getId());
        return recommendationMapper.toRequestDto(finalRequest);
    }

    private RecommendationRequest createEntity(CreateRecommendationRequestDto requestDto, User userRequester, User userReceiver) {
        RecommendationRequest request = new RecommendationRequest();
        request.setStatus(RequestStatus.PENDING);
        request.setRequester(userRequester);
        request.setReceiver(userReceiver);
        request.setMessage(requestDto.getMessage());
        return request;
    }

    public RecommendationRequestDto getRecRequest(Long id) {
        RecommendationRequest recommendationRequest = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RecommendationRequest", id));
        return recommendationMapper.toRequestDto(recommendationRequest);
    }

    public void acceptRecRequest(Long id) {
        RecommendationRequest recommendationRequest = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RecommendationRequest", id));

        Long userId = userContext.getUserId();
        if (!recommendationRequest.getReceiver().getId().equals(userId)) {
            throw new BadRequestException("Запрос может принять только тот пользователь, кому он был адресован");
        }

        if (!recommendationRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new BadRequestException("Принять запрос можно только в том случае, если он находится в статусе PENDING");
        }
        recommendationRequest.setStatus(RequestStatus.ACCEPTED);
        requestRepository.save(recommendationRequest);
        log.info("Запрос на рекомендацию принят: requestId={}, userId={}",
                recommendationRequest.getId(), userId);
    }

    public void rejectRecRequest(Long id, RejectionDto rejectionDto) {
        RecommendationRequest recommendationRequest = requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RecommendationRequest", id));

        Long userId = userContext.getUserId();
        if (!recommendationRequest.getReceiver().getId().equals(userId)) {
            throw new BadRequestException("Запрос может отклонить только тот пользователь, кому он был адресован");
        }

        if (!recommendationRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new BadRequestException("отклонить запрос можно только в том случае, если он находится в статусе PENDING");
        }

        recommendationRequest.setStatus(RequestStatus.REJECTED);
        recommendationRequest.setRejectionReason(rejectionDto.reason);
        requestRepository.save(recommendationRequest);
        log.info("Запрос на рекомендацию отклонен: requestId={}, userId={}",
                recommendationRequest.getId(), userId);
    }
}
