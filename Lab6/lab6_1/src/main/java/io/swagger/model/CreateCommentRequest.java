package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CreateCommentRequest
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-03-26T03:49:08.968760438Z[GMT]")


public class CreateCommentRequest   {
  @JsonProperty("commentator_id")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)
  private Long commentatorId = null;

  @JsonProperty("service_id")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)
  private Long serviceId = null;

  @JsonProperty("content")
  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)
  private String content = null;


  public CreateCommentRequest commentatorId(Long commentatorId) { 

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

  public CreateCommentRequest serviceId(Long serviceId) { 

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

  public CreateCommentRequest content(String content) { 

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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateCommentRequest createCommentRequest = (CreateCommentRequest) o;
    return Objects.equals(this.commentatorId, createCommentRequest.commentatorId) &&
        Objects.equals(this.serviceId, createCommentRequest.serviceId) &&
        Objects.equals(this.content, createCommentRequest.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commentatorId, serviceId, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateCommentRequest {\n");
    
    sb.append("    commentatorId: ").append(toIndentedString(commentatorId)).append("\n");
    sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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
