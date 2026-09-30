package school.faang.user_service.controller.recommendation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.recommendation.CreateRecommendationDto;
import school.faang.user_service.dto.recommendation.RecommendationDto;
import school.faang.user_service.dto.recommendation.RecommendationFilterDto;
import school.faang.user_service.dto.recommendation.UpdateRecommendationDto;
import school.faang.user_service.service.recommendation.RecommendationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping("/author/{authorId}/receiver/{receiverId}/")
    public RecommendationDto createRecommendation(
            @PathVariable Long authorId,@PathVariable Long receiverId,
            @Valid @RequestBody CreateRecommendationDto dto) {
        return recommendationService.createRecommendation(authorId, receiverId, dto);
    }

    @PutMapping("/{recommendationId}")
    public RecommendationDto updateRecommendation(@PathVariable Long recommendationId,
                                                  @Valid @RequestBody UpdateRecommendationDto recommendationDto) {
        return recommendationService.updateRecommendation(recommendationId, recommendationDto);
    }

    @DeleteMapping("/{recommendationId}")
    public void deleteRecommendation(@PathVariable Long recommendationId) {
        recommendationService.deleteRecommendation(recommendationId);
    }

    @GetMapping("/getrecommendations")
    public List<RecommendationDto> getByFiltersRecommendation
            (@RequestBody RecommendationFilterDto recommendationFilterDto) {

        return recommendationService.getByFiltersRecommendation(recommendationFilterDto);
    }
}
