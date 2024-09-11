package com.onion.backend.dto.comment;

import com.onion.backend.dto.article.Article;
import com.onion.backend.dto.user.User;
import com.onion.backend.entity.board.CommentEntity;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


public record Comment(
    Long id,
    String content,
    User author,
    Article article,
    Long parentCommentId,
    List<Comment> childComments,
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
            entity.getChildComments().isEmpty() ? Collections.emptyList()
                : entity.getChildComments().stream().map(Comment::from).toList(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

}
