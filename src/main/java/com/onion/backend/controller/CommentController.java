package com.onion.backend.controller;

import com.onion.backend.dto.comment.CreateCommentReplyRequest;
import com.onion.backend.dto.comment.CreateCommentReplyResponse;
import com.onion.backend.dto.comment.CreateCommentRequest;
import com.onion.backend.dto.comment.CreateCommentResponse;
import com.onion.backend.dto.common.dto.Response;
import com.onion.backend.service.board.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;


    @PostMapping("board/{boardId}/article/{articleId}/comment")
    public ResponseEntity<Response<CreateCommentResponse>> createComment(
        @PathVariable Long boardId, @PathVariable Long articleId, Authentication authentication,
        @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(
            Response.success(CreateCommentResponse.from(
                commentService.createComment(boardId, articleId, authentication.getName(), request))
            )
        );
    }


    @PostMapping("board/{boardId}/article/{articleId}/comment/{commentId}")
    public ResponseEntity<Response<CreateCommentReplyResponse>> createCommentReply(
        @PathVariable Long boardId, @PathVariable Long articleId, @PathVariable Long commentId,
        Authentication authentication, @RequestBody CreateCommentReplyRequest request) {

        return ResponseEntity.ok(Response.success(
            CreateCommentReplyResponse.from(
                commentService.createCommentReply(boardId, articleId, commentId,
                    authentication.getName(), request)
            )
        ));
    }


}
