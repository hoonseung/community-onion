package com.onion.backend.dto.article;

import com.onion.backend.entity.board.ArticleEntity;
import java.time.LocalDateTime;

public record ArticleResponse(
    Long id,
    Long boardId,
    Long authorId,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ArticleResponse from(ArticleEntity entity){
        return new ArticleResponse(
            entity.getId(),
            entity.getBoard().getId(),
            entity.getAuthor().getId(),
            entity.getTitle(),
            entity.getContent().getText(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
