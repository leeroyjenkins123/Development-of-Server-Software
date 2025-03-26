package io.swagger.repository;

import io.swagger.entity.CommentLikeDislikeEntity;
import io.swagger.entity.CommentatorEntity;
import io.swagger.entity.CommentEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikeDislikeRepository extends JpaRepository<CommentLikeDislikeEntity, Long> {
    Optional<CommentLikeDislikeEntity> findByCommentAndCommentator(CommentEntity comment, CommentatorEntity commentator);

    @Modifying
    @Query("DELETE FROM CommentLikeDislikeEntity cld WHERE cld.comment.id = :commentId")
    void deleteByCommentId(@Param("commentId") Long commentId);
}
