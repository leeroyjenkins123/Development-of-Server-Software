package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.exception.NotFoundException;
import io.swagger.model.Comment;
import io.swagger.model.CommentListResponse;
import io.swagger.model.CommentResponse;
import io.swagger.model.CreateCommentRequest;
import io.swagger.model.UpdateCommentRequest;
import io.swagger.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentsApiController.class)
public class CommentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldDeleteCommentSuccessfully() throws Exception {
        // Arrange
        Long commentId = 1L;
        doNothing().when(commentService).deleteComment(commentId);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/{comment_id}", commentId))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteComment(commentId);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentComment() throws Exception {
        // Arrange
        Long commentId = 1L;
        doThrow(new NotFoundException("Comment not found with id: " + commentId))
                .when(commentService).deleteComment(commentId);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/{comment_id}", commentId))
                .andExpect(status().isNotFound());

        verify(commentService, times(1)).deleteComment(commentId);
    }

    @Test
    void shouldDislikeCommentSuccessfully() throws Exception {
        // Arrange
        Long commentId = 1L;
        Long commentatorId = 2L;
        CommentResponse response = createCommentResponse(commentId, "Test comment", 0, 1);
        when(commentService.dislikeComment(commentId, commentatorId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/{comment_id}/dislike", commentId)
                        .param("commentator_id", commentatorId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.comment_id").value(commentId))
                .andExpect(jsonPath("$.data.content").value("Test comment"))
                .andExpect(jsonPath("$.data.dislikes_count").value(1));

        verify(commentService, times(1)).dislikeComment(commentId, commentatorId);
    }

    @Test
    void shouldFixDislikeCommentSuccessfully() throws Exception {
        // Arrange
        Long commentId = 1L;
        Long commentatorId = 2L;
        CommentResponse response = createCommentResponse(commentId, "Test comment", 0, 1);
        when(commentService.dislikeComment(commentId, commentatorId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/{comment_id}/dislike", commentId)
                        .param("commentator_id", commentatorId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.comment_id").value(commentId))
                .andExpect(jsonPath("$.data.content").value("Test comment"))
                .andExpect(jsonPath("$.data.dislikes_count").value(1));

        verify(commentService, times(1)).dislikeComment(commentId, commentatorId);
    }

    @Test
    void shouldReturnNotFoundWhenDislikingNonExistentComment() throws Exception {
        // Arrange
        Long commentId = 1L;
        Long commentatorId = 2L;
        when(commentService.dislikeComment(commentId, commentatorId))
                .thenThrow(new NotFoundException("Comment not found with id: " + commentId));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/{comment_id}/dislike", commentId)
                        .param("commentator_id", commentatorId.toString()))
                .andExpect(status().isNotFound());

        verify(commentService, times(1)).dislikeComment(commentId, commentatorId);
    }

    @Test
    void shouldGetCommentSuccessfully() throws Exception {
        // Arrange
        Long commentId = 1L;
        CommentResponse response = createCommentResponse(commentId, "Test comment", 0, 0);
        when(commentService.getCommentById(commentId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/comments/{comment_id}", commentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.comment_id").value(commentId))
                .andExpect(jsonPath("$.data.content").value("Test comment"));

        verify(commentService, times(1)).getCommentById(commentId);
    }

    @Test
    void shouldReturnNotFoundWhenGettingNonExistentComment() throws Exception {
        // Arrange
        Long commentId = 1L;
        when(commentService.getCommentById(commentId))
                .thenThrow(new NotFoundException("Comment not found with id: " + commentId));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/comments/{comment_id}", commentId))
                .andExpect(status().isNotFound());

        verify(commentService, times(1)).getCommentById(commentId);
    }

    @Test
    void shouldLikeCommentSuccessfully() throws Exception {
        // Arrange
        Long commentId = 1L;
        Long commentatorId = 2L;
        CommentResponse response = createCommentResponse(commentId, "Test comment", 1, 0);
        when(commentService.likeComment(commentId, commentatorId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/{comment_id}/like", commentId)
                        .param("commentator_id", commentatorId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.comment_id").value(commentId))
                .andExpect(jsonPath("$.data.content").value("Test comment"))
                .andExpect(jsonPath("$.data.likes_count").value(1));

        verify(commentService, times(1)).likeComment(commentId, commentatorId);
    }

    @Test
    void shouldReturnNotFoundWhenLikingNonExistentComment() throws Exception {
        // Arrange
        Long commentId = 1L;
        Long commentatorId = 2L;
        when(commentService.likeComment(commentId, commentatorId))
                .thenThrow(new NotFoundException("Comment not found with id: " + commentId));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/{comment_id}/like", commentId)
                        .param("commentator_id", commentatorId.toString()))
                .andExpect(status().isNotFound());

        verify(commentService, times(1)).likeComment(commentId, commentatorId);
    }

    @Test
    void shouldUpdateCommentSuccessfully() throws Exception {
        // Arrange
        Long commentId = 1L;
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent("Updated comment");
        CommentResponse response = createCommentResponse(commentId, "Updated comment", 0, 0);
        when(commentService.updateComment(eq(commentId), any(UpdateCommentRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/comments/{comment_id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.comment_id").value(commentId))
                .andExpect(jsonPath("$.data.content").value("Updated comment"));

        verify(commentService, times(1)).updateComment(eq(commentId), any(UpdateCommentRequest.class));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentComment() throws Exception {
        // Arrange
        Long commentId = 1L;
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent("Updated comment");
        when(commentService.updateComment(eq(commentId), any(UpdateCommentRequest.class)))
                .thenThrow(new NotFoundException("Comment not found with id: " + commentId));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/comments/{comment_id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(commentService, times(1)).updateComment(eq(commentId), any(UpdateCommentRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingWithEmptyContent() throws Exception {
        // Arrange
        Long commentId = 1L;
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent(""); // Пустой контент
        when(commentService.updateComment(eq(commentId), any(UpdateCommentRequest.class)))
                .thenThrow(new IllegalArgumentException("Comment content cannot be empty"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/comments/{comment_id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(commentService, times(1)).updateComment(eq(commentId), any(UpdateCommentRequest.class));
    }

    @Test
    void shouldGetCommentsSuccessfully() throws Exception {
        // Arrange
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setContent("Test comment");
        comment.setLikesCount(0);
        comment.setDislikesCount(0);
        CommentListResponse response = new CommentListResponse();
        response.setPage(1);
        response.setSize(10);
        response.setTotal(1);
        response.setData(Collections.singletonList(comment));
        when(commentService.getComments(1, 10, null, null)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.data[0].comment_id").value(1L))
                .andExpect(jsonPath("$.data[0].content").value("Test comment"));

        verify(commentService, times(1)).getComments(1, 10, null, null);
    }

    @Test
    void shouldGetCommentsWithFiltersSuccessfully() throws Exception {
        // Arrange
        Long serviceId = 1L;
        Long commentatorId = 2L;
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setContent("Test comment");
        comment.setServiceId(serviceId);
        comment.setCommentatorId(commentatorId);
        CommentListResponse response = new CommentListResponse();
        response.setPage(1);
        response.setSize(10);
        response.setTotal(1);
        response.setData(Collections.singletonList(comment));
        when(commentService.getComments(1, 10, serviceId, commentatorId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                        .param("page", "1")
                        .param("size", "10")
                        .param("service_id", serviceId.toString())
                        .param("commentator_id", commentatorId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.data[0].comment_id").value(1L))
                .andExpect(jsonPath("$.data[0].content").value("Test comment"))
                .andExpect(jsonPath("$.data[0].service_id").value(serviceId))
                .andExpect(jsonPath("$.data[0].commentator_id").value(commentatorId));

        verify(commentService, times(1)).getComments(1, 10, serviceId, commentatorId);
    }

    @Test
    void shouldCreateCommentSuccessfully() throws Exception {
        // Arrange
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("New comment");
        request.setServiceId(1L);
        request.setCommentatorId(2L);
        CommentResponse response = createCommentResponse(1L, "New comment", 0, 0);
        response.getData().setServiceId(1L);
        response.getData().setCommentatorId(2L);
        when(commentService.createComment(any(CreateCommentRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.comment_id").value(1L))
                .andExpect(jsonPath("$.data.content").value("New comment"))
                .andExpect(jsonPath("$.data.service_id").value(1L))
                .andExpect(jsonPath("$.data.commentator_id").value(2L));

        verify(commentService, times(1)).createComment(any(CreateCommentRequest.class));
    }

    @Test
    void shouldReturnNotFoundWhenCreatingCommentWithNonExistentService() throws Exception {
        // Arrange
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("New comment");
        request.setServiceId(1L);
        request.setCommentatorId(2L);
        when(commentService.createComment(any(CreateCommentRequest.class)))
                .thenThrow(new NotFoundException("Service not found"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(commentService, times(1)).createComment(any(CreateCommentRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingCommentWithEmptyContent() throws Exception {
        // Arrange
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent(""); // Пустой контент
        request.setServiceId(1L);
        request.setCommentatorId(2L);
        when(commentService.createComment(any(CreateCommentRequest.class)))
                .thenThrow(new IllegalArgumentException("Comment content cannot be empty"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(commentService, times(1)).createComment(any(CreateCommentRequest.class));
    }

    // Вспомогательный метод для создания CommentResponse
    private CommentResponse createCommentResponse(Long commentId, String content, int likesCount, int dislikesCount) {
        CommentResponse response = new CommentResponse();
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setContent(content);
        comment.setLikesCount(likesCount);
        comment.setDislikesCount(dislikesCount);
        comment.setCreatedAt(OffsetDateTime.now()); // Fixed: Use OffsetDateTime directly
        comment.setUpdatedAt(OffsetDateTime.now()); // Fixed: Use OffsetDateTime directly
        response.setData(comment);
        return response;
    }
}