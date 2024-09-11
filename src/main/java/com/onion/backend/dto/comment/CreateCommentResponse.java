package com.onion.backend.dto.comment;

public record CreateCommentResponse(
    Long id,
    Long articleId
) {

    public static CreateCommentResponse from(Comment comment) {
        return new CreateCommentResponse(
            comment.id(),
            comment.article().id()
        );
    }
}
