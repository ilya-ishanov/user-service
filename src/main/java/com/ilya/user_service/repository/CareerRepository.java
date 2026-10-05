package com.ilya.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ilya.user_service.entity.Career;

public interface CareerRepository extends JpaRepository<Career, Long> {

}
