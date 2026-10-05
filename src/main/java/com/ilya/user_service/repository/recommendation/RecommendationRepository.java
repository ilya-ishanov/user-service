package com.ilya.user_service.repository.recommendation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.ilya.user_service.entity.recommendation.Recommendation;

import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    @Query(nativeQuery = true, value = """
            INSERT INTO recommendation (author_id, receiver_id, content)
            VALUES (?1, ?2, ?3) returning id
            """)
    Long create(long authorId, long receiverId, String content);

    @Query(nativeQuery = true, value = """
            UPDATE recommendation SET content = :content, updated_at = now()
            WHERE author_id = :authorId AND receiver_id = :receiverId
            """)
    @Modifying
    void update(long authorId, long receiverId, String content);

    void deleteByIdAndAuthorId(Long recommendationId, Long authorId);

    Page<Recommendation> findAllByReceiverId(long receiverId, Pageable pageable);

    Page<Recommendation> findAllByAuthorId(long authorId, Pageable pageable);

    Optional<Recommendation> findById(Long id);

    Optional<Recommendation> findByAuthorId(Long authorId);

//    Optional<Recommendation> findFirstByAuthorIdAndReceiverIdOrderByCreatedAtDesc(Long authorId, Long receiverId);

    Optional<Recommendation> findFirstByAuthorIdAndReceiverIdOrderByCreatedAtDesc(long authorId, long receiverId);
}