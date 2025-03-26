package io.swagger.mapper;

import io.swagger.abstraction.IMapper;
import io.swagger.entity.CommentEntity;
import io.swagger.model.Comment;
import io.swagger.model.CommentResponse;
import io.swagger.model.CommentListResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import java.time.OffsetDateTime;

@Component
public class CommentMapper implements IMapper<CommentEntity, CommentResponse> {

    @Override
    public CommentResponse map(CommentEntity entity) {
        CommentResponse response = new CommentResponse();
        Comment comment = new Comment();

        comment.setCommentId(entity.getCommentId());
        comment.setContent(entity.getContent());
        comment.setCommentatorId(entity.getCommentatorId());
        comment.setServiceId(entity.getServiceId());
        comment.setLikesCount(entity.getLikesCount());
        comment.setDislikesCount(entity.getDislikesCount());
        comment.setCreatedAt(entity.getCreatedAt());
        comment.setUpdatedAt(entity.getLastEditAt());

        response.setData(comment);
        return response;
    }

    public CommentListResponse toListResponse(Page<CommentEntity> commentPage) {
        CommentListResponse response = new CommentListResponse();

        // Extract the Comment objects from CommentResponse
        List<Comment> comments = commentPage.getContent()
                .stream()
                .map(this::map) // map to CommentResponse
                .map(CommentResponse::getData) // extract Comment from CommentResponse
                .collect(Collectors.toList());

        response.setData(comments);
        response.setPage(commentPage.getNumber() + 1);
        response.setSize(commentPage.getSize());
        response.setTotal((int) commentPage.getTotalElements());

        return response;
    }

    @Override
    public CommentEntity reverseMap(CommentResponse dto) {
        CommentEntity entity = new CommentEntity();
        Comment comment = dto.getData();

        entity.setCommentId(comment.getCommentId());
        entity.setContent(comment.getContent());
        entity.setCommentatorId(comment.getCommentatorId());
        entity.setServiceId(comment.getServiceId());
        entity.setLikesCount(comment.getLikesCount());
        entity.setDislikesCount(comment.getDislikesCount());
        entity.setCreatedAt(comment.getCreatedAt());
        entity.setLastEditAt(comment.getUpdatedAt());

        return entity;
    }
}