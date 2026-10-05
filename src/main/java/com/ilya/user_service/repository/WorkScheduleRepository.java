package com.ilya.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ilya.user_service.entity.WorkSchedule;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

}
