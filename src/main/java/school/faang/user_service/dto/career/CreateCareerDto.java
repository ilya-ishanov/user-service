package school.faang.user_service.dto.career;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import school.faang.user_service.entity.User;

import java.time.LocalDate;

@Data
public class CreateCareerDto {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    @NotBlank
    private String company;
    @NotBlank
    private String position;
}
