package com.onion.backend.entity.board.repository;

import com.onion.backend.entity.board.CommentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("select c from CommentEntity c where c.author.username = :username order by c.createdAt desc limit 1")
    Optional<CommentEntity> findLatestCreateCommentByUsername(String username);

    @Query("select c from CommentEntity c where c.author.username = :username order by c.updatedAt desc limit 1")
    Optional<CommentEntity> findLatestUpdateCommentByUsername(String username);

    @Query(nativeQuery = true,
        value = "select c.* from comment c inner join users u on c.author_id = u.id "
            + "where u.username = :username and c.is_deleted = true order by c.updated_at desc limit 1")
    Optional<CommentEntity> findLatestDeleteCommentByUsername(String username);

    List<CommentEntity> findAllByArticleId(Long articleId);
}
