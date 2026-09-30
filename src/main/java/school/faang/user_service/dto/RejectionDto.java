package school.faang.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectionDto {

    @NotBlank(message = "причина не может быть пустой")
    public String reason;
}
