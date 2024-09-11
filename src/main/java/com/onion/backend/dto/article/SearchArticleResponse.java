package com.onion.backend.dto.article;

import java.time.LocalDateTime;

public record SearchArticleResponse(
    Long id,
    Long boardId,
    Long authorId,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static SearchArticleResponse from(Article article) {
        return new SearchArticleResponse(
            article.id(),
            article.board().id(),
            article.author().id(),
            article.title(),
            article.content(),
            article.createdAt(),
            article.updatedAt()
        );
    }
}
