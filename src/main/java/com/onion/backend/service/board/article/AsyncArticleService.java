package com.onion.backend.service.board.article;

import com.onion.backend.dto.article.Article;
import com.onion.backend.dto.article.SearchArticleWithCommentsResponse;
import com.onion.backend.dto.comment.Comment;
import com.onion.backend.service.board.comment.CommentService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AsyncArticleService {

    private final ArticleService articleService;
    private final CommentService commentService;


    public SearchArticleWithCommentsResponse getArticlesWithComments(Long boardId, Long articleId) {
        CompletableFuture<Article> articleFuture = articleService.getAsyncWithArticle(boardId,
                articleId)
            .exceptionally(throwable -> {
                throw new IllegalStateException("비동기 작업으로 게시글을 찾아오는 작업에 예외가 발생했습니다.", throwable);
            });

        CompletableFuture<List<Comment>> commentsFuture = commentService.getAsyncWithComments(
                articleId)
            .exceptionally(throwable -> {
                throw new IllegalStateException("비동기 작업으로 댓글을 찾아오는 작업에 예외가 발생했습니다.", throwable);
            });

        return articleFuture.thenCombine(commentsFuture, SearchArticleWithCommentsResponse::from
        ).join();
    }

}
