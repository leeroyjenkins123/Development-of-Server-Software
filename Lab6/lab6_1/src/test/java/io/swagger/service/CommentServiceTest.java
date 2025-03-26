package io.swagger.service;

import io.swagger.entity.*;
import io.swagger.exception.NotFoundException;
import io.swagger.mapper.CommentMapper;
import io.swagger.model.*;
import io.swagger.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentatorRepository commentatorRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private CommentLikeDislikeRepository commentLikeDislikeRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    private CommentEntity commentEntity;
    private CommentatorEntity commentatorEntity;
    private ServiceEntity serviceEntity;
    private CommentLikeDislikeEntity likeEntity;

    @BeforeEach
    void setUp() {
        commentatorEntity = new CommentatorEntity();
        commentatorEntity.setCommentatorId(1L);
        commentatorEntity.setName("Test User");

        serviceEntity = new ServiceEntity();
        serviceEntity.setServiceId(1L);
        serviceEntity.setName("Test Service");

        commentEntity = new CommentEntity();
        commentEntity.setCommentId(1L);
        commentEntity.setContent("Test comment");
        commentEntity.setCommentatorId(1L);
        commentEntity.setServiceId(1L);
        commentEntity.setLikesCount(0);
        commentEntity.setDislikesCount(0);

        likeEntity = new CommentLikeDislikeEntity();
        likeEntity.setId(1L);
        likeEntity.setComment(commentEntity);
        likeEntity.setCommentator(commentatorEntity);
        likeEntity.setAction("like");
    }

    @Test
    void createComment_Success() {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("Test comment");
        request.setCommentatorId(1L);
        request.setServiceId(1L);

        when(commentatorRepository.findById(1L)).thenReturn(Optional.of(commentatorEntity));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(serviceEntity));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(commentMapper.map(commentEntity)).thenReturn(new CommentResponse());

        CommentResponse response = commentService.createComment(request);

        assertNotNull(response);
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
    }

    @Test
    void createComment_CommentatorNotFound() {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setCommentatorId(99L);

        when(commentatorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.createComment(request));
    }

    @Test
    void getCommentById_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(commentEntity));
        when(commentMapper.map(commentEntity)).thenReturn(new CommentResponse());

        CommentResponse response = commentService.getCommentById(1L);

        assertNotNull(response);
    }

    @Test
    void getCommentById_NotFound() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.getCommentById(99L));
    }

    @Test
    void getComments_WithServiceId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CommentEntity> page = new PageImpl<>(List.of(commentEntity));

        when(commentRepository.findByServiceId(1L, pageable)).thenReturn(page);
        when(commentMapper.toListResponse(page)).thenReturn(new CommentListResponse());

        CommentListResponse response = commentService.getComments(1, 10, 1L, null);

        assertNotNull(response);
    }

    @Test
    void updateComment_Success() {
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent("Updated content");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(commentEntity));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(commentMapper.map(commentEntity)).thenReturn(new CommentResponse());

        CommentResponse response = commentService.updateComment(1L, request);

        assertNotNull(response);
        assertEquals("Updated content", commentEntity.getContent());
    }

    @Test
    void deleteComment_Success() {
        when(commentRepository.existsById(1L)).thenReturn(true);

        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).deleteById(1L);
        verify(commentLikeDislikeRepository, times(1)).deleteByCommentId(1L);
    }

    @Test
    void likeComment_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(commentEntity));
        when(commentatorRepository.findById(1L)).thenReturn(Optional.of(commentatorEntity));
        when(commentLikeDislikeRepository.findByCommentAndCommentator(commentEntity, commentatorEntity))
                .thenReturn(Optional.empty());
        when(commentRepository.save(commentEntity)).thenReturn(commentEntity);
        when(commentMapper.map(commentEntity)).thenReturn(new CommentResponse());

        CommentResponse response = commentService.likeComment(1L, 1L);

        assertNotNull(response);
        assertEquals(1, commentEntity.getLikesCount());
    }

    @Test
    void dislikeComment_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(commentEntity));
        when(commentatorRepository.findById(1L)).thenReturn(Optional.of(commentatorEntity));
        when(commentLikeDislikeRepository.findByCommentAndCommentator(commentEntity, commentatorEntity))
                .thenReturn(Optional.empty());
        when(commentRepository.save(commentEntity)).thenReturn(commentEntity);
        when(commentMapper.map(commentEntity)).thenReturn(new CommentResponse());

        CommentResponse response = commentService.dislikeComment(1L, 1L);

        assertNotNull(response);
        assertEquals(1, commentEntity.getDislikesCount());
    }

    @Test
    void getTopComments_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CommentEntity> page = new PageImpl<>(List.of(commentEntity));

        when(commentRepository.findTopByServiceIdOrderByLikesDesc(1L, pageable)).thenReturn(page);
        when(commentMapper.toListResponse(page)).thenReturn(new CommentListResponse());

        CommentListResponse response = commentService.getTopComments(1L, 1, 10);

        assertNotNull(response);
    }
}