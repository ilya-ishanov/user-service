package com.ilya.user_service.dto.recommendationrequest;

import lombok.Data;
import com.ilya.user_service.entity.RequestStatus;

@Data
public class RecommendationRequestDto {
    private Long id;
    private String message;
    private Long receiverId;
    private Long requesterId;
    private RequestStatus status;
}
