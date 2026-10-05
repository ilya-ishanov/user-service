package com.ilya.user_service.service.skill;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ilya.user_service.config.context.UserContext;
import com.ilya.user_service.dto.skill.CreateSkillDto;
import com.ilya.user_service.dto.skill.SkillDto;
import com.ilya.user_service.entity.Skill;
import com.ilya.user_service.entity.User;
import com.ilya.user_service.mapper.skill.SkillMapper;
import com.ilya.user_service.repository.SkillRepository;
import com.ilya.user_service.repository.UserRepository;
import com.ilya.user_service.repository.recommendation.SkillOfferRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest {

    @InjectMocks
    private SkillService skillService;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    SkillOfferRepository skillOfferRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserContext userContext;

    @Spy
    private SkillMapper skillMapper = Mappers.getMapper(SkillMapper.class);

    @Test
    public void createSkillTest() {
        Long id = 1L;
        String title = "Java";

        CreateSkillDto skillDto = new CreateSkillDto();
        skillDto.setTitle(title);

        Skill skill = new Skill();
        skill.setId(id);
        skill.setTitle(title);

        Skill lastSkill = new Skill();
        lastSkill.setId(id);
        lastSkill.setTitle(title);

        SkillDto expectedResultDto = new SkillDto();
        expectedResultDto.setId(id);
        expectedResultDto.setTitle(title);

        Mockito.when(skillRepository.existsByTitle(title)).thenReturn(false);
        Mockito.when(skillMapper.toSkillEntity(skillDto)).thenReturn(skill);
        Mockito.when(skillRepository.save(skill)).thenReturn(lastSkill);
        Mockito.when(skillMapper.toSkillDto(lastSkill)).thenReturn(expectedResultDto);

        SkillDto actualResultDto = skillService.createSkill(skillDto);

        assertNotNull(actualResultDto);
        assertEquals(id, actualResultDto.getId());
        assertEquals(title, actualResultDto.getTitle());

        Mockito.verify(skillRepository, Mockito.times(1)).save(skill);
    }

    @Test
    public void getByUserIdTest() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        List<Skill> skills = new ArrayList<>();
        List<SkillDto> skillDto = new ArrayList<>();

        Mockito.when(userRepository.findUserById(id)).thenReturn(Optional.of(user));
        Mockito.when(userContext.getUserId()).thenReturn(id);
        Mockito.when(skillRepository.findAllByUserId(id)).thenReturn(skills);
        Mockito.when(skillMapper.toListSkillDto(skills)).thenReturn(skillDto);

        List<SkillDto> resultSkills = skillService.getByUserId(id);
        assertNotNull(resultSkills);

        Mockito.verify(userRepository, Mockito.times(1)).findUserById(id);
        Mockito.verify(skillRepository, Mockito.times(1)).findAllByUserId(id);
        Mockito.verify(skillMapper, Mockito.times(1)).toListSkillDto(skills);
    }

    @Test
    public void getByUserIdUserEmptyTest() {
        Long id = 1L;
        Mockito.when(userRepository.findUserById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                skillService.getByUserId(id),
                "просмотреть предложенные скиллы может только сам пользователь");
    }
}
