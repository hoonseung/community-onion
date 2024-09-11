package com.onion.backend.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchCommentResponse(

    Long id,
    String content,
    Long authorId,
    Long articleId,
    Long parentCommentId,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SearchCommentResponse> childComments
) {


    public static SearchCommentResponse from(Comment comment) {
        return new SearchCommentResponse(
            comment.id(),
            comment.content(),
            comment.author().id(),
            comment.article().id(),
            comment.parentCommentId() == null ? null : comment.parentCommentId(),
            comment.childComments().isEmpty() ? Collections.emptyList()
                : comment.childComments().stream().map(SearchCommentResponse::from).toList()
        );
    }


}
