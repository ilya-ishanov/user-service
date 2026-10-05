package com.ilya.user_service.mapper.skill;

import org.mapstruct.Mapper;
import com.ilya.user_service.dto.skill.CreateSkillDto;
import com.ilya.user_service.dto.skill.SkillCandidateDto;
import com.ilya.user_service.dto.skill.SkillDto;
import com.ilya.user_service.entity.Skill;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    Skill toSkillEntity(CreateSkillDto skillDto);

    SkillDto toSkillDto(Skill skill);

    List<SkillDto> toListSkillDto(List<Skill> skills);

    List<SkillCandidateDto> toListSkillCandidateDto(List<Skill> skills);
}
