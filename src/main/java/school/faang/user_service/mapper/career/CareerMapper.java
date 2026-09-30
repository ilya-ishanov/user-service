package school.faang.user_service.mapper.career;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import school.faang.user_service.dto.career.CareerDto;
import school.faang.user_service.dto.career.CreateCareerDto;
import school.faang.user_service.dto.career.UpdateCareerDto;
import school.faang.user_service.entity.Career;

@Mapper(componentModel = "spring")
public interface CareerMapper {
    CareerDto toCareerDto(Career career);
    Career toCareerEntity(CreateCareerDto careerDto);

    void updateCareer(UpdateCareerDto careerDto, @MappingTarget Career career);
}
