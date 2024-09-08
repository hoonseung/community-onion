package com.onion.backend.dto.comment;

import com.onion.backend.dto.article.Article;
import com.onion.backend.dto.user.User;
import com.onion.backend.entity.board.CommentEntity;
import java.time.LocalDateTime;
import java.util.List;

public record ParentComment(
    Long id,
    String content,
    User author,
    Article article,
    List<Comment> childComments,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ParentComment from(CommentEntity entity) {
        return new ParentComment(
            entity.getId(),
            entity.getContent().getText(),
            User.from(entity.getAuthor()),
            Article.from(entity.getArticle()),
            entity.getChildComments().stream().map(Comment::from).toList(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

}
