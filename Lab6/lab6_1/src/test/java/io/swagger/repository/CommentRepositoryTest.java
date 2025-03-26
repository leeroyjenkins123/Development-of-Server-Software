package io.swagger.repository;

import io.swagger.entity.CommentEntity;
import io.swagger.entity.CommentLikeDislikeEntity;
import io.swagger.entity.CommentatorEntity;
import io.swagger.entity.ServiceEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
@TestPropertySource(properties = "spring.liquibase.change-log=classpath:/db/changelog/db.test-changelog-master.xml")
public class CommentRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("lab6")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void startContainer() {
        postgres.start();
        System.out.println("Postgres URL: " + postgres.getJdbcUrl());
    }

    @AfterAll
    static void stopContainer() {
        if (postgres != null) {
            postgres.stop();
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentatorRepository commentatorRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CommentLikeDislikeRepository likeDislikeRepository;

    private static CommentEntity testComment;
    private static CommentatorEntity testCommentator;
    private static ServiceEntity testService;

    @BeforeEach
    void setup() {
        testCommentator = new CommentatorEntity();
        testCommentator.setName("Test User");
        commentatorRepository.save(testCommentator);

        testService = new ServiceEntity();
        testService.setName("Test Service");
        serviceRepository.save(testService);

        testComment = new CommentEntity();
        testComment.setContent("Test comment");
        testComment.setCommentatorId(testCommentator.getCommentatorId());
        testComment.setServiceId(testService.getServiceId());
        testComment.setLikesCount(0);
        commentRepository.save(testComment);
    }

    @Test
    void shouldSaveAndRetrieveComment() {
        Optional<CommentEntity> foundComment = commentRepository.findById(testComment.getCommentId());

        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("Test comment");
        assertThat(foundComment.get().getServiceId()).isEqualTo(testService.getServiceId());
    }

    @Test
    void shouldUpdateComment() {
        CommentEntity savedComment = testComment;
        savedComment.setContent("Updated comment");
        savedComment.setLikesCount(1);
        commentRepository.save(savedComment);

        Optional<CommentEntity> updatedComment = commentRepository.findById(savedComment.getCommentId());
        assertThat(updatedComment).isPresent();
        assertThat(updatedComment.get().getContent()).isEqualTo("Updated comment");
        assertThat(updatedComment.get().getLikesCount()).isEqualTo(1L);
    }

    @Test
    void shouldDeleteCommentAndLikes() {
        CommentEntity newComment = new CommentEntity();
        newComment.setContent("Temp comment");
        newComment.setCommentatorId(testCommentator.getCommentatorId());
        newComment.setServiceId(testService.getServiceId());
        commentRepository.save(newComment);

        CommentLikeDislikeEntity like = new CommentLikeDislikeEntity();
        like.setComment(newComment);
        like.setCommentator(testCommentator);
        like.setAction("like");
        likeDislikeRepository.save(like);

        commentRepository.deleteById(newComment.getCommentId());
        Optional<CommentEntity> deletedComment = commentRepository.findById(newComment.getCommentId());
        assertThat(deletedComment).isEmpty();

        likeDislikeRepository.deleteByCommentId(newComment.getCommentId());
        List<CommentLikeDislikeEntity> likes = likeDislikeRepository.findAll();
        assertThat(likes).doesNotContain(like);
    }

    @Test
    void shouldFindCommentsByServiceId() {
        CommentEntity anotherComment = new CommentEntity();
        anotherComment.setContent("Another comment");
        anotherComment.setCommentatorId(testCommentator.getCommentatorId());
        anotherComment.setServiceId(testService.getServiceId());
        commentRepository.save(anotherComment);

        Page<CommentEntity> comments = commentRepository.findByServiceId(
                testService.getServiceId(),
                PageRequest.of(0, 10)
        );

        assertThat(comments.getContent()).hasSize(2);
        assertThat(comments.getContent()).contains(testComment, anotherComment);
    }

    @Test
    void shouldHandleLikeDislikeRelationship() {
        CommentLikeDislikeEntity like = new CommentLikeDislikeEntity();
        like.setComment(testComment);
        like.setCommentator(testCommentator);
        like.setAction("like");
        likeDislikeRepository.save(like);

        Optional<CommentLikeDislikeEntity> foundLike = likeDislikeRepository
                .findByCommentAndCommentator(testComment, testCommentator);

        assertThat(foundLike).isPresent();
        assertThat(foundLike.get().getAction()).isEqualTo("like");
        assertThat(foundLike.get().getComment().getCommentId()).isEqualTo(testComment.getCommentId());
    }

    @Test
    void shouldFindTopCommentsByLikes() {
        CommentEntity popularComment = new CommentEntity();
        popularComment.setContent("Popular comment");
        popularComment.setCommentatorId(testCommentator.getCommentatorId());
        popularComment.setServiceId(testService.getServiceId());
        popularComment.setLikesCount(10);
        commentRepository.save(popularComment);

        Page<CommentEntity> topComments = commentRepository.findTopByServiceIdOrderByLikesDesc(
                testService.getServiceId(),
                PageRequest.of(0, 1)
        );

        assertThat(topComments.getContent()).hasSize(1);
        assertThat(topComments.getContent().get(0).getLikesCount()).isEqualTo(10L);
    }
}