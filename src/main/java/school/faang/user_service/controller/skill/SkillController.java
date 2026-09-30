package school.faang.user_service.controller.skill;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.skill.CreateSkillDto;
import school.faang.user_service.dto.skill.SkillCandidateDto;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.service.skill.SkillService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skill")
public class SkillController {
    private final SkillService skillService;

    @PostMapping("/create")
    public SkillDto createSkill(@RequestBody @Valid CreateSkillDto skillDto) {
        return skillService.createSkill(skillDto);
    }

    @GetMapping("/{id}")
    public List<SkillDto> getByUserId(@PathVariable Long id) {
        return skillService.getByUserId(id);
    }

    @GetMapping("/offered-skills/{userId}")
    public List<SkillCandidateDto> getOfferedSkills(@PathVariable long userId) {
       return skillService.getOfferedSkills(userId);
    }

    @PostMapping("/{skillId}/user/{userId}/acquire")
    public void acquireSkillFromOffers(@PathVariable long skillId, @PathVariable long userId) {
        skillService.acquireSkillFromOffers(skillId, userId);
    }
}
