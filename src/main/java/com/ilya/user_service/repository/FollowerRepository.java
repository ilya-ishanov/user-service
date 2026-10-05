package com.ilya.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ilya.user_service.entity.User;

public interface FollowerRepository extends JpaRepository<User, Long> {
}
