package school.faang.user_service.service.skill;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.dto.skill.CreateSkillDto;
import school.faang.user_service.dto.skill.SkillCandidateDto;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.recommendation.Recommendation;
import school.faang.user_service.entity.recommendation.SkillOffer;
import school.faang.user_service.exceptions.BadRequestException;
import school.faang.user_service.exceptions.EntityNotFoundException;
import school.faang.user_service.mapper.skill.SkillMapper;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final SkillOfferRepository skillOfferRepository;
    private final UserRepository userRepository;
    private final UserContext userContext;

    public SkillDto createSkill(CreateSkillDto skillDto) {
        if (skillRepository.existsByTitle(skillDto.getTitle())) {
            throw new BadRequestException("Такой скилл уже существует, нельзя добавить в базу");
        }
        Skill skill = skillMapper.toSkillEntity(skillDto);
        Skill lastSkill = skillRepository.save(skill);
        log.info("Создан новый скилл: id={}, title={}", lastSkill.getId(), lastSkill.getTitle());
        return skillMapper.toSkillDto(lastSkill);
    }

    public List<SkillDto> getByUserId(Long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));

        if (!user.getId().equals(userContext.getUserId())) {
            throw new BadRequestException("просмотреть предложенные скиллы может только сам пользователь");
        }

        List<Skill> skills = skillRepository.findAllByUserId(id);
        return skillMapper.toListSkillDto(skills);
    }

    public List<SkillCandidateDto> getOfferedSkills(long userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        if (!user.getId().equals(userContext.getUserId())) {
            throw new BadRequestException("просмотреть предложенные скиллы может только сам пользователь");
        }

        int offersAmount;
        List<SkillCandidateDto> listCandidateDto = new ArrayList<>(); // создаем пустой лист SkillCandidateDto
        List<Skill> skills = skillRepository.findSkillsOfferedToUser(userId); // находим скилы предложенные юзеру
        List<Skill> skillsWithoutDuplicates = skills.stream().distinct().toList(); // убираем дубликаты в найденых скилах

        for(Skill skill : skillsWithoutDuplicates) { // циклом проходимся по скилам
            SkillDto skillDto = skillMapper.toSkillDto(skill); // в каждой итерации создаем skillDto мапим из skill в skillDto
            offersAmount = skillOfferRepository.countAllOffersOfSkill(skill.getId(), userId); // для каждого скила находим кол-во предложений offersAmount

            SkillCandidateDto skillCandidateDto = new SkillCandidateDto(); // создаем дто skillCandidateDto

            skillCandidateDto.setSkillDto(skillDto); // кладем в skillCandidateDto  skillDto
            skillCandidateDto.setOffersAmount(offersAmount); // кладем в skillCandidateDto  offersAmount

            listCandidateDto.add(skillCandidateDto); // добавляем skillCandidateDto в лист
        }
        return listCandidateDto;
    }

    public void acquireSkillFromOffers(long skillId, long userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        if (!user.getId().equals(userContext.getUserId())) {
            throw new BadRequestException("просмотреть предложенные скиллы может только сам пользователь");
        }

        Skill skill = skillRepository.findSkillById(skillId);
        if (skill == null) {
            throw new EntityNotFoundException("Skill", skillId);
        }

        Optional<Skill> userSkill = skillRepository.findUserSkill(skillId, userId);
        if (userSkill.isPresent()) {
            throw new BadRequestException("Такой скил уже существует у юзера");
        }

        List<SkillOffer> skillOffers = skillOfferRepository.findAllOffersOfSkill(skillId, userId);
        List<User> users = new ArrayList<>();

        for (SkillOffer skillOffer : skillOffers) {
            Recommendation recommendation = skillOffer.getRecommendation();
            User userAuthor = recommendation.getAuthor();
            users.add(userAuthor);
        }

        long countUsers = users.stream().distinct().count();
        if (countUsers < 3) {
            throw new BadRequestException("Скилл порекомендовали менее трех раз");
        } else {
            skillRepository.assignSkillToUser(skillId, userId);
            log.info("Пользователь получил предложенный скилл: userId={}, skillId={}, количество рекомендаций={}",
                    userId, skillId, countUsers);
        }
    }

}
