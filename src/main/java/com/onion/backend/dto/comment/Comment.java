package com.onion.backend.dto.comment;

import com.onion.backend.dto.article.Article;
import com.onion.backend.dto.user.User;
import com.onion.backend.entity.board.CommentEntity;
import java.time.LocalDateTime;

public record Comment(
    Long id,
    String content,
    User author,
    Article article,
    Long parentCommentId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static Comment from(CommentEntity entity) {
        return new Comment(
            entity.getId(),
            entity.getContent().getText(),
            User.from(entity.getAuthor()),
            Article.from(entity.getArticle()),
            entity.getParentComment() == null ? null : entity.getParentComment().getId(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

}
