package io.swagger.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Comment
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-03-26T03:49:08.968760438Z[GMT]")


public class Comment   {
  @JsonProperty("comment_id")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId = null;

  @JsonProperty("content")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  private String content = null;

  @JsonProperty("commentator_id")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  private Long commentatorId = null;

  @JsonProperty("service_id")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  private Long serviceId = null;

  @JsonProperty("created_at")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private OffsetDateTime createdAt = null;

  @JsonProperty("updated_at")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private OffsetDateTime updatedAt = null;

  @JsonProperty("likes_count")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  private Integer likesCount = null;

  @JsonProperty("dislikes_count")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  private Integer dislikesCount = null;


  public Comment commentId(Long commentId) { 

    this.commentId = commentId;
    return this;
  }

  /**
   * Get commentId
   * @return commentId
   **/
  
  @Schema(example = "1", required = true, description = "")
  
  @NotNull
  public Long getCommentId() {  
    return commentId;
  }



  public void setCommentId(Long commentId) { 

    this.commentId = commentId;
  }

  public Comment content(String content) { 

    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
   **/
  
  @Schema(example = "Это пример комментария", required = true, description = "")
  
  @NotNull
  public String getContent() {  
    return content;
  }



  public void setContent(String content) { 

    this.content = content;
  }

  public Comment commentatorId(Long commentatorId) { 

    this.commentatorId = commentatorId;
    return this;
  }

  /**
   * Get commentatorId
   * @return commentatorId
   **/
  
  @Schema(example = "123", required = true, description = "")
  
  @NotNull
  public Long getCommentatorId() {  
    return commentatorId;
  }



  public void setCommentatorId(Long commentatorId) { 

    this.commentatorId = commentatorId;
  }

  public Comment serviceId(Long serviceId) { 

    this.serviceId = serviceId;
    return this;
  }

  /**
   * Get serviceId
   * @return serviceId
   **/
  
  @Schema(example = "456", required = true, description = "")
  
  @NotNull
  public Long getServiceId() {  
    return serviceId;
  }



  public void setServiceId(Long serviceId) { 

    this.serviceId = serviceId;
  }

  public Comment createdAt(OffsetDateTime createdAt) { 

    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   **/
  
  @Schema(example = "2023-01-01'T'12:00:00", required = true, description = "")
  
@Valid
  @NotNull
  public OffsetDateTime getCreatedAt() {  
    return createdAt;
  }



  public void setCreatedAt(OffsetDateTime createdAt) { 

    this.createdAt = createdAt;
  }

  public Comment updatedAt(OffsetDateTime updatedAt) { 

    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   **/
  
  @Schema(example = "2023-01-01'T'12:30:00", required = true, description = "")
  
@Valid
  @NotNull
  public OffsetDateTime getUpdatedAt() {  
    return updatedAt;
  }



  public void setUpdatedAt(OffsetDateTime updatedAt) { 

    this.updatedAt = updatedAt;
  }

  public Comment likesCount(Integer likesCount) { 

    this.likesCount = likesCount;
    return this;
  }

  /**
   * Get likesCount
   * @return likesCount
   **/
  
  @Schema(example = "10", required = true, description = "")
  
  @NotNull
  public Integer getLikesCount() {  
    return likesCount;
  }



  public void setLikesCount(Integer likesCount) { 

    this.likesCount = likesCount;
  }

  public Comment dislikesCount(Integer dislikesCount) { 

    this.dislikesCount = dislikesCount;
    return this;
  }

  /**
   * Get dislikesCount
   * @return dislikesCount
   **/
  
  @Schema(example = "2", required = true, description = "")
  
  @NotNull
  public Integer getDislikesCount() {  
    return dislikesCount;
  }



  public void setDislikesCount(Integer dislikesCount) { 

    this.dislikesCount = dislikesCount;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(this.commentId, comment.commentId) &&
        Objects.equals(this.content, comment.content) &&
        Objects.equals(this.commentatorId, comment.commentatorId) &&
        Objects.equals(this.serviceId, comment.serviceId) &&
        Objects.equals(this.createdAt, comment.createdAt) &&
        Objects.equals(this.updatedAt, comment.updatedAt) &&
        Objects.equals(this.likesCount, comment.likesCount) &&
        Objects.equals(this.dislikesCount, comment.dislikesCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commentId, content, commentatorId, serviceId, createdAt, updatedAt, likesCount, dislikesCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Comment {\n");
    
    sb.append("    commentId: ").append(toIndentedString(commentId)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    commentatorId: ").append(toIndentedString(commentatorId)).append("\n");
    sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    likesCount: ").append(toIndentedString(likesCount)).append("\n");
    sb.append("    dislikesCount: ").append(toIndentedString(dislikesCount)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
