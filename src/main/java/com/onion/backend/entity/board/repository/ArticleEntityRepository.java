package com.onion.backend.entity.board.repository;

import com.onion.backend.entity.board.ArticleEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleEntityRepository extends JpaRepository<ArticleEntity, Long> {

    @Query("select a from ArticleEntity a where a.board.id = :boardId order by a.createdAt desc limit 10")
    List<ArticleEntity> findAllByBoardIdLOrderByCreatedDateDesc(Long boardId);

    @Query("select a from ArticleEntity a where a.board.id = :boardId and a.id < :articleId order by a.createdAt desc limit 10")
    List<ArticleEntity> findAllByBoardIdAndArticleIdLessThanLOrderByCreatedDateDesc(Long boardId,
        Long articleId);

    @Query("select a from ArticleEntity a where a.board.id = :boardId and a.id > :articleId order by a.createdAt desc limit 10")
    List<ArticleEntity> findAllByBoardIdAndArticleIdGreaterThanLOrderByCreatedDateDesc(Long boardId,
        Long articleId);

    @Query("select a from ArticleEntity a where a.author.username = :username order by a.createdAt desc limit 1")
    Optional<ArticleEntity> findLatestCreateArticleByUsername(String username);

    @Query("select a from ArticleEntity a where a.author.username = :username order by a.updatedAt desc limit 1")
    Optional<ArticleEntity> findLatestUpdateArticleByUsername(String username);

    @Query(nativeQuery = true,
        value = "select a.* from article a inner join users u on a.author_id = u.id "
            + "where u.username = :username and a.is_deleted = true order by a.updated_at desc limit 1")
    Optional<ArticleEntity> findLatestDeleteArticleByUsername(String username);

    Optional<ArticleEntity> findByIdAndBoardId(Long id, Long boardId);


}
