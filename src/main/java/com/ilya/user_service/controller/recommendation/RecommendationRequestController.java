package com.ilya.user_service.controller.recommendation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ilya.user_service.dto.recommendationrequest.CreateRecommendationRequestDto;
import com.ilya.user_service.dto.recommendationrequest.RecommendationRequestDto;
import com.ilya.user_service.dto.RejectionDto;
import com.ilya.user_service.service.recommendation.RecommendationRequestService;

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
