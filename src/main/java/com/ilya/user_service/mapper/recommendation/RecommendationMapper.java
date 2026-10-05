package com.ilya.user_service.mapper.recommendation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.ilya.user_service.dto.recommendation.RecommendationDto;
import com.ilya.user_service.entity.recommendation.Recommendation;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "receiver.id", target = "receiverId")
    RecommendationDto toRecommendationDto(Recommendation recommendation);
}
