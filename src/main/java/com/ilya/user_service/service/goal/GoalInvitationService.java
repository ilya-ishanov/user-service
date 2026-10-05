package com.ilya.user_service.service.goal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ilya.user_service.config.context.UserContext;
import com.ilya.user_service.dto.goal.GoalInvitationDto;
import com.ilya.user_service.entity.RequestStatus;
import com.ilya.user_service.entity.User;
import com.ilya.user_service.entity.goal.Goal;
import com.ilya.user_service.entity.goal.GoalInvitation;
import com.ilya.user_service.entity.goal.GoalStatus;
import com.ilya.user_service.exceptions.BadRequestException;
import com.ilya.user_service.exceptions.EntityNotFoundException;
import com.ilya.user_service.mapper.goal.GoalInvitationMapper;
import com.ilya.user_service.repository.UserRepository;
import com.ilya.user_service.repository.goal.GoalInvitationRepository;
import com.ilya.user_service.repository.goal.GoalRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoalInvitationService {
    private final GoalRepository goalRepository;
    private final GoalInvitationRepository goalInvitationRepository;
    private final UserContext userContext;
    private final UserRepository userRepository;
    private final GoalInvitationMapper mapper;

    @Transactional
    public GoalInvitationDto createGoalInvitation(Long goalId, Long invitedId) {
        User inviter = userRepository.findById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        User invited = userRepository.findById(invitedId)
                .orElseThrow(() -> new EntityNotFoundException("User", invitedId));

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new EntityNotFoundException("Goal", goalId));

        if (inviter.getId().equals(invited.getId())) {
            throw new BadRequestException("Вы не можете пригласить самого себя");
        }

        User mentor = goal.getMentor();
        List<User> mentees = goal.getUsers();
        boolean isInviterIsMentor = false;
        if (mentor != null) {
            isInviterIsMentor = mentor.getId().equals(inviter.getId());
        }
        boolean isInviterIncluded = mentees.stream()
                .anyMatch(mentee -> mentee.getId().equals(inviter.getId()));
        if (!isInviterIsMentor && !isInviterIncluded) {
            throw new BadRequestException("Вы никак не относитесь к этой цели");
        }

        boolean isInvitedIsMentor = false;
        if (mentor != null) {
            isInvitedIsMentor = mentor.getId().equals(invited.getId());
        }
        boolean isInvitedIncluded = mentees.stream()
                .anyMatch(mentee -> mentee.getId().equals(invited.getId()));
        if (isInvitedIsMentor || isInvitedIncluded) {
            throw new BadRequestException("Вы не должны работать над данной целью ни в роли ментора, ни в роли участника");
        }

        GoalInvitation goalInvitation = new GoalInvitation();
        goalInvitation.setGoal(goal);
        goalInvitation.setInviter(inviter);
        goalInvitation.setInvited(invited);
        goalInvitation.setStatus(RequestStatus.PENDING);
        goalInvitationRepository.save(goalInvitation);
        log.info("Создано приглашение в цель: goalInvitationId={}, goalId={}, inviterId={}, invitedId={}",
                goalInvitation.getId(),
                goalId,
                inviter.getId(),
                invitedId);
        return mapper.toGoalInvitationDto(goalInvitation);
    }

    @Transactional
    public void acceptInvitation(Long invitationId) {
        User userFromContext = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() ->  new EntityNotFoundException("User", userContext.getUserId()));

        List<Goal> goalsUser = goalRepository.findGoalsByUserId(userFromContext.getId()).toList();
        long countActiveStatus = goalsUser.stream()
                .filter(goal -> goal.getStatus().equals(GoalStatus.ACTIVE)).count();
        if (countActiveStatus >= 3) {
            throw new BadRequestException("Пользователь не может принять приглашение, если у него 3 или более активных целей");
        }

        GoalInvitation goalInvitation = goalInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new EntityNotFoundException("GoalInvitation", invitationId));
        Goal goal = goalInvitation.getGoal();

        boolean isItIncludeInGoalUsers = goal.getUsers().stream()
                .anyMatch(user -> user.getId().equals(userFromContext.getId()));
        if (isItIncludeInGoalUsers) {
            throw new BadRequestException("Вы присоединились к этой цели ранее");
        }

        User invitedUser = goalInvitation.getInvited();
        if (!invitedUser.getId().equals(userContext.getUserId())) {
            throw new BadRequestException("Принять или отклонить приглашение может только тот пользователь, кому оно было адресовано");
        }

        goal.getUsers().add(invitedUser);
        goalRepository.save(goal);
        goalInvitation.setStatus(RequestStatus.ACCEPTED);
        goalInvitationRepository.save(goalInvitation);
        log.info("Приглашение принято: invitationId={}, goalId={}, userId={}",
                invitationId, goal.getId(), userFromContext.getId());
    }

    @Transactional
    public void rejectInvitation(Long invitationId) {
        User userFromContext = userRepository.findUserById(userContext.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", userContext.getUserId()));

        GoalInvitation goalInvitation = goalInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new EntityNotFoundException("GoalInvitation", invitationId));

        User invitedUser = goalInvitation.getInvited();
        if (!invitedUser.getId().equals(userFromContext.getId())) {
            throw new BadRequestException("Отклонить приглашение может только тот пользователь, кому оно было адресовано");
        }

        goalInvitation.setStatus(RequestStatus.REJECTED);
        goalInvitationRepository.save(goalInvitation);
        log.info("Приглашение отклонено: invitationId={}, userId={}",
                invitationId, userFromContext.getId());
    }
}
