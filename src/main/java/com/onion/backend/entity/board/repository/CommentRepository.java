package com.onion.backend.entity.board.repository;

import com.onion.backend.entity.board.CommentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("select c from CommentEntity c where c.author.username = :username order by c.createdAt desc limit 1")
    Optional<CommentEntity> findLatestCreateCommentByUsername(String username);

    @Query("select c from CommentEntity c where c.author.username = :username order by c.updatedAt desc limit 1")
    Optional<CommentEntity> findLatestUpdateCommentByUsername(String username);

    boolean existsByAuthorUsername(String username);

}
