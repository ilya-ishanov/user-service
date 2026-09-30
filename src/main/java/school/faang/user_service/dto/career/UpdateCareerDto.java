package school.faang.user_service.dto.career;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCareerDto {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    @NotBlank
    private String company;
    @NotBlank
    private String position;
}
