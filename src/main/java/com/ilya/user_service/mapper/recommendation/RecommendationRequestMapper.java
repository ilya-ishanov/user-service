package com.ilya.user_service.mapper.recommendation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.ilya.user_service.dto.recommendationrequest.RecommendationRequestDto;
import com.ilya.user_service.entity.recommendation.RecommendationRequest;

@Mapper(componentModel = "spring")
public interface RecommendationRequestMapper {
    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "receiver.id", target = "receiverId")
    RecommendationRequestDto toRequestDto(RecommendationRequest recommendationRequest);
}
