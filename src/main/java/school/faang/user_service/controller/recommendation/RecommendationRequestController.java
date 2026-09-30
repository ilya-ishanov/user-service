package school.faang.user_service.controller.recommendation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.recommendationrequest.CreateRecommendationRequestDto;
import school.faang.user_service.dto.recommendationrequest.RecommendationRequestDto;
import school.faang.user_service.dto.RejectionDto;
import school.faang.user_service.service.recommendation.RecommendationRequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rec-request")
public class RecommendationRequestController {
    private final RecommendationRequestService requestService;

    @PostMapping
    public RecommendationRequestDto createRecRequest
            (@Valid @RequestBody CreateRecommendationRequestDto requestDto) {
        return requestService.createRecRequest(requestDto);
    }

    @GetMapping("/{id}")
    public RecommendationRequestDto getRecRequest(@PathVariable Long id) {
        return requestService.getRecRequest(id);
    }

    @PostMapping("/accept/{id}")
    public void acceptRecRequest(@PathVariable Long id) {
        requestService.acceptRecRequest(id);
    }

    @PostMapping("/reject/{id}")
    public void rejectRecRequest(@PathVariable Long id,
                                 @Valid @RequestBody RejectionDto rejectionDto) {
        requestService.rejectRecRequest(id, rejectionDto);
    }
}
