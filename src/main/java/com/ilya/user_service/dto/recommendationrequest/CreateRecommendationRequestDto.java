package com.ilya.user_service.dto.recommendationrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRecommendationRequestDto {
    @NotBlank(message = "message не может быть пустым")
    private String message;
    @NotNull
    private Long receiver_id;
}
