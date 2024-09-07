package com.onion.backend.service.board.article;

import com.onion.backend.dto.article.ArticleResponse;
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
import lombok.RequiredArgsConstructor;
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


    public ArticleResponse getArticle(Long articleId, Long boardId) {
        return articleEntityRepository.findByIdAndBoardId(articleId, boardId)
            .map(ArticleResponse::from)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
    }


    public List<ArticleResponse> getArticles(Long boardId) {
        return articleEntityRepository.findAllByBoardIdLOrderByCreatedDateDesc(boardId).stream()
            .map(ArticleResponse::from)
            .toList();
    }

    public List<ArticleResponse> getOldArticles(Long boardId, Long articleId) {
        return articleEntityRepository.findAllByBoardIdAndArticleIdLessThanLOrderByCreatedDateDesc(
                boardId, articleId)
            .stream()
            .map(ArticleResponse::from)
            .toList();
    }

    public List<ArticleResponse> getLatestArticles(Long boardId, Long articleId) {
        return articleEntityRepository.findAllByBoardIdAndArticleIdGreaterThanLOrderByCreatedDateDesc(
                boardId, articleId)
            .stream()
            .map(ArticleResponse::from)
            .toList();
    }


    private boolean canNotWriteArticle(String username) {
        ArticleEntity articlePs = articleEntityRepository.findLatestCreateArticleByUsername(
                username)
            .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        Duration duration = Duration.between(articlePs.getCreatedAt(), LocalDateTime.now());

        return duration.toMinutes() < 5;
    }

    private boolean canNotUpdateArticle(String username) {
        ArticleEntity articlePs = articleEntityRepository.findLatestUpdateArticleByUsername(
                username)
            .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        Duration duration = Duration.between(articlePs.getUpdatedAt(), LocalDateTime.now());

        return duration.toMinutes() < 5;
    }

    @Transactional
    public void deleteArticle(Long articleId, Long boardId, String username) {
        if (canNotUpdateArticle(username)) {
            throw new TimeRateLimitException("5분 이내에 삭제한 게시글이 있습니다.");
        }
        articleEntityRepository.findByIdAndBoardId(articleId, boardId)
            .ifPresent(article -> {
                article.getAuthor().checkUsername(username);
                articleEntityRepository.delete(article);
            });
    }
}
