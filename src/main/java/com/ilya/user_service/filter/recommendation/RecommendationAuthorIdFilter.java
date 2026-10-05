package com.ilya.user_service.filter.recommendation;

import org.springframework.stereotype.Component;
import com.ilya.user_service.dto.recommendation.RecommendationFilterDto;
import com.ilya.user_service.entity.recommendation.Recommendation;

import java.util.stream.Stream;

@Component
public class RecommendationAuthorIdFilter implements RecommendationFilter {
    @Override
    public boolean isApplicable(RecommendationFilterDto recommendationFilterDto) {
        return recommendationFilterDto.getAuthorId() != null;
    }

    @Override
    public Stream<Recommendation> apply(Stream<Recommendation> stream, RecommendationFilterDto recommendationFilterDto) {
        return stream.filter(recommendation -> recommendationFilterDto
                .getAuthorId().equals(recommendation.getAuthor().getId()));
    }
}
