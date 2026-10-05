package com.ilya.user_service.filter.recommendation;

import com.ilya.user_service.dto.recommendation.RecommendationFilterDto;
import com.ilya.user_service.entity.recommendation.Recommendation;

import java.util.stream.Stream;

public interface RecommendationFilter {
    boolean isApplicable(RecommendationFilterDto recommendationFilterDto);

    Stream<Recommendation> apply(Stream<Recommendation> stream, RecommendationFilterDto recommendationFilterDto);
}
