package com.ilya.user_service.controller.education;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ilya.user_service.dto.education.CreateEducationDto;
import com.ilya.user_service.dto.education.EducationDto;
import com.ilya.user_service.dto.education.UpdateEducationDto;
import com.ilya.user_service.service.education.EducationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/education")
public class EducationController {
    private final EducationService educationService;

    @PostMapping
    public EducationDto createEducation(@Valid @RequestBody CreateEducationDto educationDto) {
        return educationService.createEducation(educationDto);
    }

    @PutMapping("/{educationId}")
    public EducationDto updateEducation(@Valid @RequestBody UpdateEducationDto updateDto,
                                        @PathVariable Long educationId) {
        return educationService.updateEducation(updateDto, educationId);
    }

    @GetMapping("/{educationId}")
    public EducationDto getEducationById(@PathVariable Long educationId) {
        return educationService.getEducationById(educationId);
    }
}
