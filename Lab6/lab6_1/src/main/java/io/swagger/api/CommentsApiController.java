package io.swagger.api;

import io.swagger.model.CommentListResponse;
import io.swagger.model.CommentResponse;
import io.swagger.model.CreateCommentRequest;
import io.swagger.model.UpdateCommentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.exception.NotFoundException;
import io.swagger.service.CommentService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-03-26T03:49:08.968760438Z[GMT]")
@RestController
public class CommentsApiController implements CommentsApi {

    private static final Logger log = LoggerFactory.getLogger(CommentsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final CommentService commentService;

    @org.springframework.beans.factory.annotation.Autowired
    public CommentsApiController(ObjectMapper objectMapper, HttpServletRequest request, CommentService commentService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.commentService = commentService;
    }

    public ResponseEntity<Void> commentsCommentIdDelete(@Parameter(in = ParameterIn.PATH, description = "ID комментария", required=true, schema=@Schema()) @PathVariable("comment_id") Long commentId
) {
        try {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            log.error("Comment not found with id: {}", commentId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error deleting comment with id: {}", commentId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommentResponse> commentsCommentIdDislikePost(@Parameter(in = ParameterIn.PATH, description = "ID комментария", required=true, schema=@Schema()) @PathVariable("comment_id") Long commentId
,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID пользователя, совершающего действие (лайк/дизлайк)" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "commentator_id", required = true) Long commentatorId
) {
        try {
            CommentResponse response = commentService.dislikeComment(commentId, commentatorId);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Comment not found: {}", commentId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error getting comment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommentResponse> commentsCommentIdGet(@Parameter(in = ParameterIn.PATH, description = "ID комментария", required=true, schema=@Schema()) @PathVariable("comment_id") Long commentId
) {
        try {
            CommentResponse response = commentService.getCommentById(commentId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error("Comment not found: {}", commentId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error getting comment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommentResponse> commentsCommentIdLikePost(@Parameter(in = ParameterIn.PATH, description = "ID комментария", required=true, schema=@Schema()) @PathVariable("comment_id") Long commentId
,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID пользователя, совершающего действие (лайк/дизлайк)" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "commentator_id", required = true) Long commentatorId
) {
        try {
            CommentResponse response = commentService.likeComment(commentId, commentatorId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error("Comment not found: {}", commentId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error adding like: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommentResponse> commentsCommentIdPut(@Parameter(in = ParameterIn.PATH, description = "ID комментария", required=true, schema=@Schema()) @PathVariable("comment_id") Long commentId
,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody UpdateCommentRequest body
) {
        try {
            CommentResponse response = commentService.updateComment(commentId, body);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error("Comment not found: {}", commentId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error updating comment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommentListResponse> commentsGet(@Parameter(in = ParameterIn.QUERY, description = "Номер страницы" ,schema=@Schema( defaultValue="1")) @Valid @RequestParam(value = "page", required = false, defaultValue="1") Integer page
,@Parameter(in = ParameterIn.QUERY, description = "Размер страницы" ,schema=@Schema( defaultValue="10")) @Valid @RequestParam(value = "size", required = false, defaultValue="10") Integer size
,@Parameter(in = ParameterIn.QUERY, description = "ID сервиса" ,schema=@Schema()) @Valid @RequestParam(value = "service_id", required = false) Long serviceId
,@Parameter(in = ParameterIn.QUERY, description = "ID пользователя (необязательно для фильтрации)" ,schema=@Schema()) @Valid @RequestParam(value = "commentator_id", required = false) Long commentatorId
) {
        try {
            CommentListResponse response = commentService.getComments(page, size, serviceId, commentatorId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting comments: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<CommentResponse> commentsPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody CreateCommentRequest body
) {
        try {
            CommentResponse response = commentService.createComment(body);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            log.error("Resource not found: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error creating comment: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
