package com.ilya.user_service.dto.recommendation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRecommendationDto {

    @NotBlank(message = "Рекомендация не должна быть пустой")
    private String content;
}
