package school.faang.user_service.dto.recommendation;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateRecommendationDto {
    @NotEmpty(message = "Рекомендация не должна быть пустой")
    private String content;
}
