package com.onion.backend.service.board.article;

import com.onion.backend.dto.article.Article;
import com.onion.backend.dto.article.CreateArticleRequest;
import com.onion.backend.dto.article.UpdateArticleRequest;
import com.onion.backend.dto.common.exception.TimeRateLimitException;
import com.onion.backend.entity.board.ArticleEntity;
import com.onion.backend.entity.board.repository.ArticleEntityRepository;
import com.onion.backend.entity.board.repository.BoardEntityRepository;
import com.onion.backend.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleService {

    private final BoardEntityRepository boardEntityRepository;
    private final ArticleEntityRepository articleEntityRepository;
    private final UserService userService;


    @Transactional
    public Long createArticle(CreateArticleRequest request, String username, Long boardId) {
        if (canNotWriteArticle(username)) {
            throw new TimeRateLimitException("5분 이내에 작성한 게시글이 있습니다.");
        }
        ArticleEntity articlePs = ArticleEntity.of(request.title(), request.text(),
            userService.getUser(username),
            boardEntityRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시판입니다."))
        );
        articleEntityRepository.save(articlePs);

        return articlePs.getId();
    }


    @Transactional
    public Long editArticle(Long articleId, Long boardId, UpdateArticleRequest request,
        String username) {
        if (canNotUpdateArticle(username)) {
            throw new TimeRateLimitException("5분 이내에 수정한 게시글이 있습니다.");
        }
        articleEntityRepository.findByIdAndBoardId(articleId, boardId)
            .ifPresent(article -> {
                article.getAuthor().checkUsername(username);
                article.edit(request.title(), request.text());
            });
        return articleId;
    }


    @Async
    public CompletableFuture<Article> getAsyncWithArticle(Long articleId, Long boardId) {
        ArticleEntity articlePs = getArticleEntity(boardId, articleId);

        return CompletableFuture.completedFuture(Article.from(articlePs));
    }


    public ArticleEntity getArticleEntity(Long boardId, Long articleId) {
        return articleEntityRepository.findByIdAndBoardId(articleId, boardId)
            .orElseThrow(() -> new EntityNotFoundException(
                String.format("%d 번 게시판에 존재하지 않는 게시글입니다.", boardId)));
    }


    public List<Article> getArticles(Long boardId) {
        return articleEntityRepository.findAllByBoardIdLOrderByCreatedDateDesc(boardId).stream()
            .map(Article::from)
            .toList();
    }

    public List<Article> getOldArticles(Long boardId, Long articleId) {
        return articleEntityRepository.findAllByBoardIdAndArticleIdLessThanLOrderByCreatedDateDesc(
                boardId, articleId)
            .stream()
            .map(Article::from)
            .toList();
    }

    public List<Article> getLatestArticles(Long boardId, Long articleId) {
        return articleEntityRepository.findAllByBoardIdAndArticleIdGreaterThanLOrderByCreatedDateDesc(
                boardId, articleId)
            .stream()
            .map(Article::from)
            .toList();
    }


    private boolean canNotWriteArticle(String username) {
        Optional<ArticleEntity> articlePs = articleEntityRepository
            .findLatestCreateArticleByUsername(username);
        if (articlePs.isPresent()) {
            Duration duration = Duration.between(articlePs.get().getCreatedAt(),
                LocalDateTime.now());
            return duration.toMinutes() < 5;
        }
        return false;
    }

    private boolean canNotUpdateArticle(String username) {
        ArticleEntity articlePs = articleEntityRepository.findLatestUpdateArticleByUsername(
                username)
            .orElseThrow(() -> new EntityNotFoundException("최신 게시글을 찾을 수 없습니다."));

        Duration duration = Duration.between(articlePs.getUpdatedAt(), LocalDateTime.now());

        return duration.toMinutes() < 5;
    }

    private boolean canNotDeleteArticle(String username) {
        Optional<ArticleEntity> articlePs = articleEntityRepository.findLatestDeleteArticleByUsername(
            username);
        if (articlePs.isPresent()) {
            Duration duration = Duration.between(articlePs.get().getUpdatedAt(),
                LocalDateTime.now());
            return duration.toMinutes() < 5;
        }
        return false;
    }

    @Transactional
    public void deleteArticle(Long articleId, Long boardId, String username) {
        if (canNotDeleteArticle(username)) {
            throw new TimeRateLimitException("5분 이내에 삭제한 게시글이 있습니다.");
        }
        articleEntityRepository.findByIdAndBoardId(articleId, boardId)
            .ifPresent(article -> {
                article.getAuthor().checkUsername(username);
                articleEntityRepository.delete(article);
            });
    }
}
