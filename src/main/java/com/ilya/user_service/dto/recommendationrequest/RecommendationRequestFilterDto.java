package com.ilya.user_service.dto.recommendationrequest;

import com.ilya.user_service.entity.RequestStatus;

public class RecommendationRequestFilterDto {
    private Long receiver_id;
    private Long requester_id;
    private String message;
    private RequestStatus status;
}
