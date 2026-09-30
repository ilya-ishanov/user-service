package school.faang.user_service.dto.recommendationrequest;

import school.faang.user_service.entity.RequestStatus;

public class RecommendationRequestFilterDto {
    private Long receiver_id;
    private Long requester_id;
    private String message;
    private RequestStatus status;
}
