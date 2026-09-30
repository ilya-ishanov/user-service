package school.faang.user_service.dto.recommendation;

import lombok.Data;

@Data
public class RecommendationFilterDto {
    private String contentContains;
    private Long authorId;
    private Long receiverId;
}
