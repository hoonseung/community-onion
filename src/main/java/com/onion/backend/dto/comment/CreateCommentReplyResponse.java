package com.onion.backend.dto.comment;

public record CreateCommentReplyResponse(
    Long id,
    Long articleId,
    Long parentCommentId
) {

    public static CreateCommentReplyResponse from(Comment comment) {
        return new CreateCommentReplyResponse(
            comment.id(),
            comment.article().id(),
            comment.parentCommentId()
        );
    }
}
