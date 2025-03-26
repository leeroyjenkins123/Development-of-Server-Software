package io.swagger.entity;

import javax.persistence.*;

@Entity
@Table(name = "commentators")
public class CommentatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentator_id", updatable = false, nullable = false)
    private Long commentatorId;

    @Column(name = "name", nullable = false)
    private String name;

    public CommentatorEntity(){

    }

    public CommentatorEntity(Long commentatorId, String name){
        this.commentatorId = commentatorId;
        this.name = name;
    }

    public Long getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(Long commentatorId) {
        this.commentatorId = commentatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
