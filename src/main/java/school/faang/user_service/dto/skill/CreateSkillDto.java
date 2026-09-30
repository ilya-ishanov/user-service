package school.faang.user_service.dto.skill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSkillDto {

    @NotBlank(message = "Название скилла не должно быть пустым")
    @Size(min = 2, max = 50, message = "Название скилла должно содержать от 2 до 50 символов")
    private String title;
}
