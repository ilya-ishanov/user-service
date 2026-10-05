package com.ilya.user_service.service.education;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.ilya.user_service.config.context.UserContext;
import com.ilya.user_service.dto.education.CreateEducationDto;
import com.ilya.user_service.dto.education.EducationDto;
import com.ilya.user_service.dto.education.UpdateEducationDto;
import com.ilya.user_service.entity.Education;
import com.ilya.user_service.entity.User;
import com.ilya.user_service.exceptions.BadRequestException;
import com.ilya.user_service.exceptions.EntityNotFoundException;
import com.ilya.user_service.mapper.education.EducationMapper;
import com.ilya.user_service.repository.EducationRepository;
import com.ilya.user_service.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationService {
    private final EducationMapper educationMapper;
    private final UserContext userContext;
    private final UserRepository userRepository;
    private final EducationRepository educationRepository;

    @Transactional
    public EducationDto createEducation(CreateEducationDto educationDto) {
        Integer yearFrom = educationDto.getYearFrom();
        Integer yearNow = LocalDate.now().getYear();
        if (yearFrom > yearNow) {
            throw new BadRequestException("Год начала обучения " + yearFrom + " должен быть не больше текущего года");
        }

        Education education = educationMapper.toEducationEntity(educationDto);
        User userFromContext = userRepository.findById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));
        education.setUser(userFromContext);
        educationRepository.save(education);
        log.info("Добавлено образование: educationId={}, userId={}",
                education.getId(), userFromContext.getId());
        return educationMapper.toEducationDto(education);
    }

    @Transactional
    public EducationDto updateEducation(UpdateEducationDto updateDto, Long educationId) {
        Integer yearFrom = updateDto.getYearFrom();
        Integer yearNow = LocalDate.now().getYear();
        if (yearFrom > yearNow) {
            throw new BadRequestException("Год начала обучения " + yearFrom + " должен быть не больше текущего года");
        }

        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new EntityNotFoundException("Education", educationId));

        User user = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));
        if (!education.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Обновлять можно только свои данные");
        }

        educationMapper.updateEducation(updateDto, education);
        educationRepository.save(education);
        log.info("Обновлено образование: educationId={}, userId={}", educationId, user.getId());
        return educationMapper.toEducationDto(education);
    }

    public EducationDto getEducationById(Long educationId) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new EntityNotFoundException("Education", educationId));

        return educationMapper.toEducationDto(education);
    }
}
