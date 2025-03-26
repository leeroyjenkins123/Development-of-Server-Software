// CommentatorRepository.java
package io.swagger.repository;

import io.swagger.entity.CommentatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentatorRepository extends JpaRepository<CommentatorEntity, Long> {
}