// CommentRepository.java
package io.swagger.repository;

import io.swagger.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findByServiceId(Long serviceId, Pageable pageable);
    Page<CommentEntity> findByCommentatorId(Long commentatorId, Pageable pageable);
    Page<CommentEntity> findByServiceIdAndCommentatorId(Long serviceId, Long commentatorId, Pageable pageable);

    @Query("SELECT c FROM CommentEntity c WHERE c.serviceId = :serviceId ORDER BY c.likesCount DESC")
    Page<CommentEntity> findTopByServiceIdOrderByLikesDesc(Long serviceId, Pageable pageable);
}