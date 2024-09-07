package com.onion.backend.service.board.article;

import com.onion.backend.dto.article.ArticleResponse;
import com.onion.backend.dto.article.CreateArticleRequest;
import com.onion.backend.entity.board.ArticleEntity;
import com.onion.backend.entity.board.repository.ArticleEntityRepository;
import com.onion.backend.entity.board.repository.BoardEntityRepository;
import com.onion.backend.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
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
        ArticleEntity articlePs = ArticleEntity.of(request.title(), request.text(),
            userService.getUser(username),
            boardEntityRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시판입니다."))
        );
        articleEntityRepository.save(articlePs);

        return articlePs.getId();
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
}
