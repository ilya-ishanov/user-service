package school.faang.user_service.service.mentorship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.entity.Mentorship;
import school.faang.user_service.entity.User;
import school.faang.user_service.exceptions.BadRequestException;
import school.faang.user_service.exceptions.EntityNotFoundException;
import school.faang.user_service.mapper.user.UserMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.mentorship.MentorshipRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentorshipService {
    private final MentorshipRepository mentorshipRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserContext userContext;

    public void addMentorship(Long mentorId, Long menteeId) {
        User mentor = userRepository.findById(mentorId).orElseThrow(
                () -> new EntityNotFoundException("User", mentorId));

        User mentee = userRepository.findById(menteeId).orElseThrow(
                () -> new EntityNotFoundException("User", menteeId));

        if (mentor.getId().equals(mentee.getId())) {
            throw new BadRequestException("пользователь не может быть ментором для самого себя");
        }

        Optional<Mentorship> mentorshipOptional = mentorshipRepository
                .findByMentorIdAndMenteeId(mentor.getId(), mentee.getId());

        if (mentorshipOptional.isPresent()) {
            throw new BadRequestException("Связь между %d и %d уже существует".formatted(mentor.getId(), mentee.getId()));
        }

        Mentorship mentorship = new Mentorship();
        mentorship.setMentor(mentor);
        mentorship.setMentee(mentee);
        mentorshipRepository.save(mentorship);
        log.info("Создана связь ментор-менти: mentorId={}, menteeId={}",
                mentorId, menteeId);
    }

    public List<UserDto> getMentees(Long mentorId) {
        User mentor = userRepository.findUserById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("User", mentorId));

        if (mentor.getMentees().isEmpty()) {
            throw new BadRequestException("Нет менти у ментора, mentorId - " + mentorId);
        }

        return userMapper.toUserDto(mentor.getMentees());
    }

    public List<UserDto> getMentors(Long menteeId) {
        User mentee = userRepository.findUserById(menteeId)
                .orElseThrow(() -> new EntityNotFoundException("User", menteeId));

        if (mentee.getMentors().isEmpty()) {
            throw new BadRequestException("Нет менторов у менти, menteeId - " + menteeId);
        }

        return userMapper.toUserDto(mentee.getMentors());
    }

    public void delete(Long menteeId, Long mentorId) {
        User mentee = userRepository.findUserById(menteeId)
                .orElseThrow(() -> new EntityNotFoundException("User", menteeId));

        User mentor = userRepository.findUserById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("User", mentorId));

        Mentorship mentorship = mentorshipRepository
                .findByMentorIdAndMenteeId(mentor.getId(), mentee.getId())
                .orElseThrow(() -> new BadRequestException("Такой связи не существует"));

        if (mentee.getId().equals(userContext.getUserId()) || mentor.getId().equals(userContext.getUserId())) {
            mentorshipRepository.delete(mentorship);
            log.info("Удалена связь ментор-менти: mentorId={}, menteeId={}", mentorId, menteeId);
        } else {
            throw new BadRequestException("Вы не можете удалить эту связь потому что не являетесь ментором или менти этой связи");
        }
    }
}
