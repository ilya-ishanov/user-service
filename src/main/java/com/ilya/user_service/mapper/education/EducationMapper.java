package com.ilya.user_service.mapper.education;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.ilya.user_service.dto.education.CreateEducationDto;
import com.ilya.user_service.dto.education.EducationDto;
import com.ilya.user_service.dto.education.UpdateEducationDto;
import com.ilya.user_service.entity.Education;

@Mapper(componentModel = "spring")
public interface EducationMapper {
    EducationDto toEducationDto(Education education);
    Education toEducationEntity(CreateEducationDto educationDto);

    void updateEducation(UpdateEducationDto updateEducationDto, @MappingTarget Education educationEntity);
}
