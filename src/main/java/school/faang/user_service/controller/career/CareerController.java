package school.faang.user_service.controller.career;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.career.CareerDto;
import school.faang.user_service.dto.career.CreateCareerDto;
import school.faang.user_service.dto.career.UpdateCareerDto;
import school.faang.user_service.service.career.CareerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/career")
public class CareerController {
    private final CareerService careerService;

    @PostMapping
    public CareerDto addCareer(@Valid @RequestBody CreateCareerDto createCareerDto) {
        return careerService.addCareer(createCareerDto);
    }

    @PutMapping("/{careerId}")
    public CareerDto updateCareer(@Valid @RequestBody UpdateCareerDto updateDto,
                                  @PathVariable Long careerId) {
        return careerService.updateCareer(updateDto,careerId);
    }

    @GetMapping("/{careerId}")
    public CareerDto getCareerById(@PathVariable Long careerId) {
        return careerService.getCareerById(careerId);
    }
}
