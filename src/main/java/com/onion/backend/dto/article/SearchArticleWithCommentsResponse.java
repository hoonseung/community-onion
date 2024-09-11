package com.onion.backend.dto.article;

import com.onion.backend.dto.comment.Comment;
import com.onion.backend.dto.comment.SearchCommentResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record SearchArticleWithCommentsResponse(
    Long id,
    Long boardId,
    Long authorId,
    String title,
    String content,
    List<SearchCommentResponse> comments,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {


    public static SearchArticleWithCommentsResponse from(
        Article article,
        List<Comment> comments) {
        return new SearchArticleWithCommentsResponse(
            article.id(),
            article.board().id(),
            article.author().id(),
            article.title(),
            article.content(),
            comments.stream().filter(comment -> Objects.isNull(comment.parentCommentId()))
                .map(SearchCommentResponse::from).toList(),
            article.createdAt(),
            article.updatedAt()
        );
    }
}
