package io.swagger.service;

import io.swagger.entity.CommentEntity;
import io.swagger.entity.CommentLikeDislikeEntity;
import io.swagger.entity.CommentatorEntity;
import io.swagger.entity.ServiceEntity;
import io.swagger.exception.NotFoundException;
import io.swagger.mapper.CommentMapper;
import io.swagger.model.*;
import io.swagger.repository.CommentLikeDislikeRepository;
import io.swagger.repository.CommentRepository;
import io.swagger.repository.CommentatorRepository;
import io.swagger.repository.ServiceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentatorRepository commentatorRepository;
    private final ServiceRepository serviceRepository;
    private final CommentMapper commentMapper;
    private final CommentLikeDislikeRepository commentLikeDislikeRepository;
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    public CommentService(CommentRepository commentRepository,
                          CommentatorRepository commentatorRepository,
                          ServiceRepository serviceRepository,
                          CommentMapper commentMapper, CommentLikeDislikeRepository commentLikeDislikeRepository) {
        this.commentRepository = commentRepository;
        this.commentatorRepository = commentatorRepository;
        this.serviceRepository = serviceRepository;
        this.commentMapper = commentMapper;
        this.commentLikeDislikeRepository = commentLikeDislikeRepository;
    }

    @Transactional
    public CommentResponse createComment(CreateCommentRequest request) {
        log.info("Creating comment: {}", request);
        try {
            CommentatorEntity commentator = commentatorRepository.findById(request.getCommentatorId())
                    .orElseThrow(() -> new NotFoundException("Commentator not found"));

            ServiceEntity service = serviceRepository.findById(request.getServiceId())
                    .orElseThrow(() -> new NotFoundException("Service not found"));

            CommentEntity comment = new CommentEntity();
            comment.setContent(request.getContent());
            comment.setCommentatorId(commentator.getCommentatorId());
            comment.setServiceId(service.getServiceId());

            CommentEntity savedComment = commentRepository.save(comment);
            log.info("Comment created: {}", savedComment);

            return commentMapper.map(savedComment);
        } catch (Exception e) {
            log.error("Error creating comment: {}",e.getMessage(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));
        return commentMapper.map(comment);
    }

    @Transactional(readOnly = true)
    public CommentListResponse getComments(Integer page, Integer size, Long serviceId, Long commentatorId) {
        PageRequest pageRequest = buildPageRequest(page, size);
        Page<CommentEntity> commentsPage = findComments(serviceId, commentatorId, pageRequest);

        return commentMapper.toListResponse(commentsPage);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request) {
        validateUpdateRequest(request);

        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));

        updateCommentEntity(comment, request);
        CommentEntity updatedComment = commentRepository.save(comment);

        return commentMapper.map(updatedComment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
        commentLikeDislikeRepository.deleteByCommentId(commentId);
    }

    @Transactional
    public CommentResponse likeComment(Long commentId, Long commentatorId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));

        log.info("Попытка найти комментатора с ID: {}", commentatorId);
        CommentatorEntity commentator = commentatorRepository.findById(commentatorId)
                .orElseThrow(() -> new IllegalArgumentException("Комментатор с ID " + commentatorId + " не найден"));
        log.info("Найден комментатор: id={}, name={}", commentator.getCommentatorId(), commentator.getName());

        if (commentator.getName() == null) {
            throw new IllegalStateException("Имя комментатора не может быть null");
        }

        CommentLikeDislikeEntity existingLikeDislike = commentLikeDislikeRepository.findByCommentAndCommentator(comment, commentator)
                .orElse(null);

        if (existingLikeDislike != null) {
            if ("like".equals(existingLikeDislike.getAction())) {
                log.info("Comment {} already liked by commentator {}", commentId, commentatorId);
                return commentMapper.map(comment);
            } else {
                existingLikeDislike.setAction("like");
                commentLikeDislikeRepository.save(existingLikeDislike);
                incrementLikes(comment);
                decrementDislikes(comment);
            }
        } else {
            CommentLikeDislikeEntity like = new CommentLikeDislikeEntity();
            like.setComment(comment);
            like.setCommentator(commentator);
            like.setAction("like");
            log.info("Перед сохранением: commentator id={}, name={}",
                    like.getCommentator().getCommentatorId(), like.getCommentator().getName());
            commentLikeDislikeRepository.save(like);
            incrementLikes(comment);
        }

        CommentEntity updatedComment = commentRepository.save(comment);
        return commentMapper.map(updatedComment);
    }

    @Transactional
    public CommentResponse dislikeComment(Long commentId, Long commentatorId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));

        CommentatorEntity commentator = commentatorRepository.findById(commentatorId)
                .orElseThrow(() -> new NotFoundException("Commentator not found"));

        // Проверка, не поставил ли уже этот комментатор лайк или дизлайк
        CommentLikeDislikeEntity existingLikeDislike = commentLikeDislikeRepository.findByCommentAndCommentator(comment, commentator)
                .orElse(null);

        if (existingLikeDislike != null) {
            if ("dislike".equals(existingLikeDislike.getAction())) {
                log.info("Comment {} already disliked by commentator {}", commentId, commentatorId);
                return commentMapper.map(comment); // Дизлайк уже поставлен, ничего не меняем
            } else {
                // Обновляем с лайка на дизлайк
                existingLikeDislike.setAction("dislike");
                commentLikeDislikeRepository.save(existingLikeDislike);
                incrementDislikes(comment);
                decrementLikes(comment);
            }
        } else {
            // Если не существует записи, создаем новую
            CommentLikeDislikeEntity dislike = new CommentLikeDislikeEntity();
            dislike.setComment(comment);
            dislike.setCommentator(commentator);
            dislike.setAction("dislike");
            commentLikeDislikeRepository.save(dislike);
            incrementDislikes(comment);
        }

        CommentEntity updatedComment = commentRepository.save(comment);
        return commentMapper.map(updatedComment);
    }

    @Transactional(readOnly = true)
    public CommentListResponse getTopComments(Long serviceId, Integer page, Integer size) {
        PageRequest pageRequest = buildPageRequest(page, size);
        Page<CommentEntity> commentsPage = commentRepository.findTopByServiceIdOrderByLikesDesc(serviceId, pageRequest);

        return commentMapper.toListResponse(commentsPage);
    }

    // Вспомогательные методы
    private void validateRequest(CreateCommentRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
    }

    private void validateUpdateRequest(UpdateCommentRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
    }

    private CommentEntity buildCommentEntity(CreateCommentRequest request,
                                             CommentatorEntity commentator,
                                             ServiceEntity service) {
        CommentEntity comment = new CommentEntity();
        comment.setContent(request.getContent());
        comment.setCommentatorId(commentator.getCommentatorId());
        comment.setServiceId(service.getServiceId());
        comment.setLikesCount(0);
        comment.setDislikesCount(0);
        return comment;
    }

    private PageRequest buildPageRequest(Integer page, Integer size) {
        return PageRequest.of(
                page != null ? page - 1 : 0,
                size != null ? size : 10
        );
    }

    private Page<CommentEntity> findComments(Long serviceId, Long commentatorId, PageRequest pageRequest) {
        if (serviceId != null && commentatorId != null) {
            return commentRepository.findByServiceIdAndCommentatorId(serviceId, commentatorId, pageRequest);
        } else if (serviceId != null) {
            return commentRepository.findByServiceId(serviceId, pageRequest);
        } else if (commentatorId != null) {
            return commentRepository.findByCommentatorId(commentatorId, pageRequest);
        }
        return commentRepository.findAll(pageRequest);
    }

    private void updateCommentEntity(CommentEntity comment, UpdateCommentRequest request) {
        comment.setContent(request.getContent());
    }

    // Увеличение количества лайков
    private void incrementLikes(CommentEntity comment) {
        comment.setLikesCount(comment.getLikesCount() + 1);
    }

    // Увеличение количества дизлайков
    private void incrementDislikes(CommentEntity comment) {
        comment.setDislikesCount(comment.getDislikesCount() + 1);
    }

    // Уменьшение количества лайков
    private void decrementLikes(CommentEntity comment) {
        comment.setLikesCount(comment.getLikesCount() - 1);
    }

    // Уменьшение количества дизлайков
    private void decrementDislikes(CommentEntity comment) {
        comment.setDislikesCount(comment.getDislikesCount() - 1);
    }
}
