package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CommentListResponse
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-03-26T03:49:08.968760438Z[GMT]")


public class CommentListResponse   {
  @JsonProperty("data")
  @Valid
  private List<Comment> data = null;
  @JsonProperty("page")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer page = null;

  @JsonProperty("size")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer size = null;

  @JsonProperty("total")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer total = null;


  public CommentListResponse data(List<Comment> data) { 

    this.data = data;
    return this;
  }

  public CommentListResponse addDataItem(Comment dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<Comment>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   **/
  
  @Schema(description = "")
  @Valid
  public List<Comment> getData() {  
    return data;
  }



  public void setData(List<Comment> data) { 
    this.data = data;
  }

  public CommentListResponse page(Integer page) { 

    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
   **/
  
  @Schema(example = "1", description = "")
  
  public Integer getPage() {  
    return page;
  }



  public void setPage(Integer page) { 
    this.page = page;
  }

  public CommentListResponse size(Integer size) { 

    this.size = size;
    return this;
  }

  /**
   * Get size
   * @return size
   **/
  
  @Schema(example = "10", description = "")
  
  public Integer getSize() {  
    return size;
  }



  public void setSize(Integer size) { 
    this.size = size;
  }

  public CommentListResponse total(Integer total) { 

    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   **/
  
  @Schema(example = "100", description = "")
  
  public Integer getTotal() {  
    return total;
  }



  public void setTotal(Integer total) { 
    this.total = total;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentListResponse commentListResponse = (CommentListResponse) o;
    return Objects.equals(this.data, commentListResponse.data) &&
        Objects.equals(this.page, commentListResponse.page) &&
        Objects.equals(this.size, commentListResponse.size) &&
        Objects.equals(this.total, commentListResponse.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, page, size, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommentListResponse {\n");
    
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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
