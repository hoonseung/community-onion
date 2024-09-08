package com.onion.backend.controller;

import com.onion.backend.dto.article.SearchArticleResponse;
import com.onion.backend.dto.article.CreateArticleRequest;
import com.onion.backend.dto.article.UpdateArticleRequest;
import com.onion.backend.dto.common.dto.Response;
import com.onion.backend.service.board.article.ArticleService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<Response<SearchArticleResponse>> getArticle(@PathVariable Long boardId,
        @PathVariable Long articleId) {
        return ResponseEntity.ok(
            Response.success(articleService.getArticle(articleId, boardId).toSearchArticleResponse()
            )
        );
    }


    @GetMapping("/board/{boardId}/articles")
    public ResponseEntity<Response<List<SearchArticleResponse>>> getLatestArticles(
        @PathVariable Long boardId,
        @RequestParam(required = false) Long lastId,
        @RequestParam(required = false) Long firstId) {
        if (Objects.isNull(lastId) && Objects.isNull(firstId)) {
            return ResponseEntity.ok(
                Response.success(
                    articleService.getArticles(boardId).stream().map(SearchArticleResponse::from)
                        .toList()
            ));
        }

        return ResponseEntity.ok(
            Response.success(
                Objects.nonNull(lastId) ? articleService.getOldArticles(boardId, lastId).stream().map(SearchArticleResponse::from)
                        .toList() :
                    articleService.getLatestArticles(boardId, firstId).stream().map(SearchArticleResponse::from)
                        .toList()
        ));
    }


    @PutMapping("/board/{boardId}/article/{articleId}")
    public ResponseEntity<Response<Long>> editArticle(@PathVariable Long boardId,
        @PathVariable Long articleId, @RequestBody UpdateArticleRequest request,
        Authentication authentication) {
        return ResponseEntity.ok(Response.success(
            articleService.editArticle(articleId, boardId, request, authentication.getName())
        ));
    }


    @DeleteMapping("/board/{boardId}/article/{articleId}")
    public ResponseEntity<Response<Void>> deleteArticle(@PathVariable Long boardId,
        @PathVariable Long articleId, Authentication authentication) {
        articleService.deleteArticle(articleId, boardId, authentication.getName());
        return ResponseEntity.ok(Response.success(null));
    }


}
