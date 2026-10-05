package com.ilya.user_service.dto.skill;

import lombok.Data;

@Data
public class SkillCandidateDto {
    private SkillDto skillDto;
    private int offersAmount;
}
