package school.faang.user_service.dto.education;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateEducationDto {
    @NotNull
    private Integer yearFrom;
    @NotNull
    private Integer yearTo;
    @NotBlank
    private String institution;
    @NotBlank
    private String educationLevel;
    @NotBlank
    private String specialization;
}
