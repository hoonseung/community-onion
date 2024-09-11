package com.onion.backend.controller;

import com.onion.backend.dto.article.SearchArticleWithCommentsResponse;
import com.onion.backend.dto.common.dto.Response;
import com.onion.backend.service.board.article.AsyncArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@RestController
public class ArticleWithCommentController {

    private final AsyncArticleService asyncArticleService;


    @GetMapping("/board/{boardId}/article/{articleId}")
    public ResponseEntity<Response<SearchArticleWithCommentsResponse>> getArticleWithComments(
        @PathVariable Long boardId,
        @PathVariable Long articleId) {
        return ResponseEntity.ok(
            Response.success(
                asyncArticleService.getArticlesWithComments(boardId, articleId)
            )
        );
    }
}
