package com.ilya.user_service.service.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ilya.user_service.config.context.UserContext;
import com.ilya.user_service.dto.recommendation.CreateRecommendationDto;
import com.ilya.user_service.dto.recommendation.RecommendationDto;
import com.ilya.user_service.dto.recommendation.RecommendationFilterDto;
import com.ilya.user_service.dto.recommendation.UpdateRecommendationDto;
import com.ilya.user_service.entity.User;
import com.ilya.user_service.entity.recommendation.Recommendation;
import com.ilya.user_service.exceptions.BadRequestException;
import com.ilya.user_service.exceptions.EntityNotFoundException;
import com.ilya.user_service.filter.recommendation.RecommendationFilter;
import com.ilya.user_service.mapper.recommendation.RecommendationMapper;
import com.ilya.user_service.repository.UserRepository;
import com.ilya.user_service.repository.recommendation.RecommendationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {
    private final RecommendationMapper recommendationMapper;
    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final UserContext userContext;
    private final List<RecommendationFilter> recommendationFilters;

    @Transactional
    public RecommendationDto createRecommendation(Long authorId, Long receiverId, CreateRecommendationDto dto) {
        User author = userRepository.findUserById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User", authorId));

        User receiver = userRepository.findUserById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("User", receiverId));

        if (receiver.equals(author)) {
            throw new BadRequestException("Пользователь не может оставить отзыв самому себе");
        }

        Optional<Recommendation> recommendation = recommendationRepository
                .findFirstByAuthorIdAndReceiverIdOrderByCreatedAtDesc(authorId, receiverId);

        Long recommendationId;
        if (recommendation.isEmpty()) {
            recommendationId = recommendationRepository.create(authorId, receiverId, dto.getContent());
        } else {

            LocalDateTime createdTime = recommendation.get().getCreatedAt();
            LocalDateTime newDate = createdTime.plusMonths(6);
            LocalDateTime timeNow = LocalDateTime.now();

            if (timeNow.isAfter(newDate) || timeNow.isEqual(newDate)) {
                recommendationId = recommendationRepository.create(authorId, receiverId, dto.getContent());
            } else {
                throw new BadRequestException("Прошло меньше шести месяцев, вы не можете оставить отзыв");
            }
        }

        Recommendation recommendationEntity = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new EntityNotFoundException("Recommendation", recommendationId));
        log.info("Создана рекомендация: recommendationId={}, authorId={}, receiverId={}",
                recommendationEntity.getId(),
                authorId,
                receiverId);
        return recommendationMapper.toRecommendationDto(recommendationEntity);
    }

    @Transactional
    public RecommendationDto updateRecommendation(Long recommendationId, UpdateRecommendationDto recommendationDto) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new EntityNotFoundException("Recommendation", recommendationId));

        Long userId = userContext.getUserId();

        if (!(userId.equals(recommendation.getAuthor().getId()))) {
            throw new BadRequestException("Только автор рекомендации может ее обновлять.");
        }

        recommendation.setContent(recommendationDto.getContent());
        Recommendation updateRecommendation = recommendationRepository.save(recommendation);
        log.info("Обновлена рекомендация: recommendationId={}, authorId={}",
                recommendationId, userId);
        return recommendationMapper.toRecommendationDto(updateRecommendation);
    }

    @Transactional
    public void deleteRecommendation(Long recommendationId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new EntityNotFoundException("Recommendation", recommendationId));

        Long userId = userContext.getUserId();

        if (!(userId.equals(recommendation.getAuthor().getId()))) {
            throw new BadRequestException("Только автор рекомендации может удалять ее.");
        }
        recommendationRepository.deleteByIdAndAuthorId(recommendationId, userId);
        log.info("Удалена рекомендация: recommendationId={}, userId={}",
                recommendationId, userId);
    }

    public List<RecommendationDto> getByFiltersRecommendation(RecommendationFilterDto recommendationFilterDto) {
        Stream<Recommendation> allRecommendation = recommendationRepository.findAll().stream();

        for (RecommendationFilter recommendationFilter : recommendationFilters) {
            if (recommendationFilter.isApplicable(recommendationFilterDto)) {
                allRecommendation = recommendationFilter.apply(allRecommendation, recommendationFilterDto);
            }
        }

        return allRecommendation
                .map(recommendationMapper::toRecommendationDto)
                .toList();
    }

}
