package school.faang.user_service.controller.goal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.goal.CreateGoalDto;
import school.faang.user_service.dto.goal.GoalDto;
import school.faang.user_service.dto.goal.UpdateGoalDto;
import school.faang.user_service.service.goal.GoalService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goal")
public class GoalController {
    private final GoalService goalService;

    @PostMapping
    public GoalDto createGoal(@Valid @RequestBody CreateGoalDto goalDto) {
        return goalService.createGoal(goalDto);
    }

    @PutMapping("/{goalId}")
    public void updateGoal(@Valid @RequestBody UpdateGoalDto dto, @PathVariable Long goalId) {
        goalService.updateGoal(dto, goalId);
    }

    @DeleteMapping("/{goalId}")
    public void deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
    }

    @GetMapping("/{mentorId}")
    public List<GoalDto> findAll(@PathVariable Long mentorId) {
        return goalService.findAll(mentorId);
    }
}
