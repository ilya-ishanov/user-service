package school.faang.user_service.dto.mentorship;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import school.faang.user_service.dto.user.UserDto;

@Data
public class CreateMentorshipRequestDto {

    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull
    private Long receiverId;
}
