package school.faang.user_service.service.career;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.dto.career.CareerDto;
import school.faang.user_service.dto.career.CreateCareerDto;
import school.faang.user_service.dto.career.UpdateCareerDto;
import school.faang.user_service.entity.Career;
import school.faang.user_service.entity.User;
import school.faang.user_service.exceptions.BadRequestException;
import school.faang.user_service.exceptions.EntityNotFoundException;
import school.faang.user_service.exceptions.NotFoundException;
import school.faang.user_service.mapper.career.CareerMapper;
import school.faang.user_service.repository.CareerRepository;
import school.faang.user_service.repository.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CareerService {
    private final UserRepository userRepository;
    private final UserContext userContext;
    private final CareerRepository careerRepository;
    private final CareerMapper careerMapper;

    public CareerDto addCareer(CreateCareerDto createCareerDto) {
        LocalDate dateFrom = createCareerDto.getDateFrom();
        LocalDate dateNow = LocalDate.now();

        if (dateFrom.isAfter(dateNow)) {
            throw new BadRequestException("Дата начала работы " + dateFrom + " не может быть в будущем");
        }

        Career career = careerMapper.toCareerEntity(createCareerDto);
        User user = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));
        career.setUser(user);
        careerRepository.save(career);
        log.info("Добавлен новый карьерный опыт: careerId={}, userId={}",
                career.getId(), user.getId());
        return careerMapper.toCareerDto(career);
    }

    public CareerDto updateCareer(UpdateCareerDto updateDto, Long careerId) {
        LocalDate dateFrom = updateDto.getDateFrom();
        LocalDate dateNow = LocalDate.now();
        if (dateFrom.isAfter(dateNow)) {
            throw new BadRequestException("Дата начала работы " + dateFrom + " не может быть в будущем");
        }

        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("Career", careerId));
        User user = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));
        if (!career.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Обновлять можно только свои данные");
        }

        careerMapper.updateCareer(updateDto, career);
        careerRepository.save(career);
        log.info("Обновлен карьерный опыт: careerId={}, userId={}", careerId, user.getId());
        return careerMapper.toCareerDto(career);
    }

    public CareerDto getCareerById(Long careerId) {
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("Career", careerId));

        return careerMapper.toCareerDto(career);
    }
}
