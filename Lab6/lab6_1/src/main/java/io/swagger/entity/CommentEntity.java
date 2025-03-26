package io.swagger.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.time.OffsetDateTime;


@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", updatable = false, nullable = false)
    private Long commentId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "commentator_id", updatable = false, nullable = false)
    private Long commentatorId;

    @Column(name = "service_id", updatable = false, nullable = false)
    private Long serviceId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_edit_at")
    private OffsetDateTime lastEditAt;

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount = 0;

    @Column(name = "dislikes_count", nullable = false)
    private Integer dislikesCount = 0;

    public CommentEntity() {
    }

    public Long getCommentId(){
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentatorId(){
        return commentatorId;
    }

    public void setCommentatorId(Long commentatorId) {
        this.commentatorId = commentatorId;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public Long getServiceId(){
        return serviceId;
    }

    public void setServiceId(Long serviceId){
        this.serviceId = serviceId;
    }

    public Integer getLikesCount(){
        return likesCount;
    }

    public void setLikesCount(Integer likesCount){
        this.likesCount = likesCount;
    }

    public Integer getDislikesCount(){
        return dislikesCount;
    }

    public void setDislikesCount(Integer dislikesCount){
        this.dislikesCount = dislikesCount;
    }

    public OffsetDateTime getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt){
        this.createdAt = createdAt;
    }

    public OffsetDateTime getLastEditAt() {
        return lastEditAt;
    }

    public void setLastEditAt(OffsetDateTime lastEditAt) {
        this.lastEditAt = lastEditAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", commentBody='" + content + '\'' +
                ", likesCount=" + likesCount +
                ", dislikesCount=" + dislikesCount +
                ", createdAt=" + createdAt +
                ", lastEditAt=" + lastEditAt +
                ", commenterId=" + commentatorId +
                ", serviceId=" + serviceId +
                '}';
    }

}
