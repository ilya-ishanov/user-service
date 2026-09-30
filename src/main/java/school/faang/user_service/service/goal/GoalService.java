package school.faang.user_service.service.goal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.dto.goal.CreateGoalDto;
import school.faang.user_service.dto.goal.GoalDto;
import school.faang.user_service.dto.goal.UpdateGoalDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.entity.goal.GoalStatus;
import school.faang.user_service.exceptions.BadRequestException;
import school.faang.user_service.exceptions.EntityNotFoundException;
import school.faang.user_service.mapper.goal.GoalMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.goal.GoalRepository;
import school.faang.user_service.repository.mentorship.MentorshipRepository;
import school.faang.user_service.repository.mentorship.MentorshipRequestRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoalService {
    private final UserContext userContext;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final GoalMapper goalMapper;

    public GoalDto createGoal(CreateGoalDto goalDto) {
        User user = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        List<Long> usersDtoIds = goalDto.getUsersIds();
        Goal goal = goalMapper.toGoalEntity(goalDto);
        if (goalDto.getMentorId() != null) {
            if (!goalDto.getMentorId().equals(user.getId())) {
                throw new BadRequestException("Пользователь не является ментором");
            }

            List<User> lastUsers = user.getMentees()
                    .stream().filter(mentee -> usersDtoIds.contains(mentee.getId())).toList();

            if (lastUsers.size() != usersDtoIds.size()) {
                throw new BadRequestException("Один из пользователей не является вашим менти");
            }

            for (Long userId : usersDtoIds) {
                int countActiveGoals = goalRepository.countActiveGoalsPerUser(userId);
                if (countActiveGoals >= 2) {
                    throw new BadRequestException("У пользователя с ID " + userId + " слишком много активных целей!");
                }
            }

            goal.setMentor(user);
            goal.setUsers(lastUsers);
        } else {
            goal.setMentor(null);
            goal.setUsers(List.of(user));
        }
        Goal savedGoal = goalRepository.save(goal);
        log.info("Создана цель: goalId={}, userId={}",
                goal.getId(), user.getId());
        return goalMapper.toGoalDto(savedGoal);
    }

    public void updateGoal(UpdateGoalDto dto, Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new EntityNotFoundException("Goal", goalId));

        User user = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        if (goal.getStatus().equals(GoalStatus.COMPLETED)) {
            throw new BadRequestException("Цель завершена, невозможно обновить");
        }

        User mentor = goal.getMentor();

        if (mentor != null) {
            boolean isMentor = mentor.getId().equals(user.getId());
            boolean isUserIncluded = goal.getUsers().stream()
                    .anyMatch(mentee -> mentee.getId().equals(user.getId()));
            if (!isUserIncluded && !isMentor) {
                throw new BadRequestException("Нет прав на обновление цели");
            }
        }

        if (mentor == null) {
            boolean isUserIncluded = goal.getUsers().stream()
                    .anyMatch(mentee -> mentee.getId().equals(user.getId()));
            if (!isUserIncluded) {
                throw new BadRequestException("Не один менти не соответствует юзеру, Вы не являетесь менти");
            }
        }

        goalMapper.updateGoal(dto, goal);
        goalRepository.save(goal);
        log.info("Обновлена цель: goalId={}, userId={}",
                goalId, user.getId());
    }

    public void deleteGoal(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new EntityNotFoundException("Goal", goalId));

        User user = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        boolean isUserIncluded = goal.getUsers().stream()
                .anyMatch(mentee -> mentee.getId().equals(user.getId()));
        int countUsers = goal.getUsers().size();

        if (goal.getMentor() == null) {
            if (!isUserIncluded) {
                throw new BadRequestException("Нет прав на удаление цели");
            }

            if (countUsers > 1) {
                goal.getUsers().remove(user);
                goalRepository.save(goal);
                log.info("Пользователь покинул цель: goalId={}, userId={}",
                        goalId, user.getId());
                return;
            }
            if (countUsers == 1) {
                goalRepository.delete(goal);
                log.info("Удалена цель: goalId={}, userId={}",
                        goalId, user.getId());
                return;
            }
        }

        if (goal.getMentor() != null) {
            User mentor = goal.getMentor();
            boolean isMentor = mentor.getId().equals(user.getId());

            if (isMentor) {
                goalRepository.delete(goal);
                log.info("Ментор удалил цель: goalId={}, userId={}",
                        goalId, user.getId());
                return;
            }

            if (isUserIncluded) {
                if (countUsers > 1) {
                    goal.getUsers().remove(user);
                    goalRepository.save(goal);
                    log.info("Пользователь покинул цель: goalId={}, userId={}",
                            goalId, user.getId());
                    return;
                }

                if (countUsers == 1) {
                    goalRepository.delete(goal);
                    log.info("Удалена цель: goalId={}, userId={}",
                            goalId, user.getId());
                    return;
                }
            }
            throw new BadRequestException("Нет прав на удаление цели");
        }
    }

    @Transactional
    public List<GoalDto> findAll(Long mentorId) {
        List<Goal> goals = goalRepository.findGoalsByMentorId(mentorId);
        if (goals.isEmpty()) {
            throw new BadRequestException("Нет целей для mentorId - " + mentorId);
        }

        return goalMapper.toListGoalDto(goals);
    }
}