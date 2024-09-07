package com.onion.backend.controller;

import com.onion.backend.dto.article.ArticleResponse;
import com.onion.backend.dto.article.CreateArticleRequest;
import com.onion.backend.dto.common.dto.Response;
import com.onion.backend.service.board.article.ArticleService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/board/{boardId}/article")
    public ResponseEntity<Response<Long>> createArticle(@RequestBody CreateArticleRequest request,
        @PathVariable Long boardId, Authentication authentication) {
        return ResponseEntity.ok(Response.success(
            articleService.createArticle(request, authentication.getName(), boardId))
        );
    }


    @GetMapping("/board/{boardId}/article/{articleId}")
    public ResponseEntity<Response<ArticleResponse>> getArticle(@PathVariable Long boardId,
        @PathVariable Long articleId) {
        return ResponseEntity.ok(
            Response.success(articleService.getArticle(articleId, boardId))
        );
    }


    @GetMapping("/board/{boardId}/articles")
    public ResponseEntity<Response<List<ArticleResponse>>> getLatestArticles(
        @PathVariable Long boardId,
        @RequestParam(required = false) Long lastId,
        @RequestParam(required = false) Long firstId) {
        if (Objects.isNull(lastId) && Objects.isNull(firstId)){
            return ResponseEntity.ok(
                Response.success(
                    articleService.getArticles(boardId)
                )
            );
        }

        return ResponseEntity.ok(
            Response.success(
                Objects.nonNull(lastId) ? articleService.getOldArticles(boardId, lastId) :
                    articleService.getLatestArticles(boardId, firstId)
            )
        );
    }

}
