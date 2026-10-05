package com.ilya.user_service.repository.mentorship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ilya.user_service.entity.Mentorship;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorshipRepository extends JpaRepository<Mentorship, Long> {
    Optional<Mentorship> findByMentorIdAndMenteeId(Long mentorId, Long menteeId);

    Optional<Mentorship> findByMentorId(Long mentorId);

    @Query(nativeQuery = true, value = """ 
    SELECT mentee_id FROM mentorship
    WHERE mentor_id = :mentorId
    """)
    List<Long> findMenteeByMentorId(Long mentorId);
}
