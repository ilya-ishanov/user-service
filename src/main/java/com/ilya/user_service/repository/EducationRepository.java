package com.ilya.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ilya.user_service.entity.Education;

public interface EducationRepository extends JpaRepository<Education, Long> {

}
