package com.onion.backend.dto.article;

import com.onion.backend.dto.board.Board;
import com.onion.backend.dto.comment.Comment;
import com.onion.backend.dto.user.User;
import com.onion.backend.entity.board.ArticleEntity;
import java.time.LocalDateTime;
import java.util.List;

public record Article(
    Long id,
    Board board,
    User author,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static Article from(ArticleEntity entity) {
        return new Article(
            entity.getId(),
            Board.from(entity.getBoard()),
            User.from(entity.getAuthor()),
            entity.getTitle(),
            entity.getContent().getText(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }


    public SearchArticleResponse toSearchArticleResponse() {
        return new SearchArticleResponse(
            this.id,
            this.board.id(),
            this.author.id(),
            this.title,
            this.content,
            this.createdAt,
            this.updatedAt
        );
    }


}
