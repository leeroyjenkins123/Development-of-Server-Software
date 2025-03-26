package io.swagger.entity;

import javax.persistence.*;


@Entity
@Table(name = "comment_likes_dislikes")
public class CommentLikeDislikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private CommentEntity comment;

    @ManyToOne
    @JoinColumn(name = "commentator_id",nullable = false)
    private CommentatorEntity commentator;

    @Column(name ="action", nullable = false)
    private String action; // "like", "dislike", "none"

    public CommentLikeDislikeEntity(){

    }

    public CommentLikeDislikeEntity(Long id, CommentEntity commentEntity, CommentatorEntity commentatorEntity, String action){
        this.id = id;
        this.comment = commentEntity;
        this.commentator = commentatorEntity;
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public CommentEntity getComment(){
        return comment;
    }

    public void setComment(CommentEntity commentEntity){
        this.comment = commentEntity;
    }

    public CommentatorEntity getCommentator(){
        return commentator;
    }

    public void setCommentator(CommentatorEntity commentatorEntity){
        this.commentator = commentatorEntity;
    }
}