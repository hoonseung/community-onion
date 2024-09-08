package com.onion.backend.service.board.comment;

import com.onion.backend.dto.comment.Comment;
import com.onion.backend.dto.comment.CreateCommentReplyRequest;
import com.onion.backend.dto.comment.CreateCommentRequest;
import com.onion.backend.dto.common.exception.TimeRateLimitException;
import com.onion.backend.entity.board.CommentEntity;
import com.onion.backend.entity.board.repository.CommentRepository;
import com.onion.backend.service.board.article.ArticleService;
import com.onion.backend.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserService userService;


    @Transactional
    public Comment createComment(Long boardId, Long articleId, String username,
        CreateCommentRequest request) {
        if (canNotWriteComment(username)) {
            throw new TimeRateLimitException("1분 이내에 작성한 댓글이 있습니다.");
        }
        CommentEntity commentPs = commentRepository.save(
            CommentEntity.of(request.text(), userService.getUser(username),
                articleService.getArticleEntity(boardId, articleId))
        );
        return Comment.from(commentPs);
    }

    @Transactional
    public Comment createCommentReply(Long boardId, Long articleId, Long commentId,
        String username, CreateCommentReplyRequest request) {
        if (canNotWriteComment(username)) {
            throw new TimeRateLimitException("1분 이내에 작성한 댓글이 있습니다.");
        }
        CommentEntity commentPs = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("부모 댓글을 찾을 수 없습니다."));
        CommentEntity commentEntity = CommentEntity.of(
            request.text(),
            userService.getUser(username),
            articleService.getArticleEntity(boardId, articleId)
        );

        commentPs.addComment(commentEntity);

        return Comment.from(commentRepository.save(commentEntity));
    }


    private boolean canNotWriteComment(String username) {
        if (commentRepository.existsByAuthorUsername(username)) {
            CommentEntity commentPs = commentRepository.findLatestCreateCommentByUsername(username)
                .orElseThrow(() -> new RuntimeException("최신 댓글을 찾을 수 없습니다."));

            Duration duration = Duration.between(commentPs.getCreatedAt(), LocalDateTime.now());

            return duration.toMinutes() < 1;
        }
        return false;
    }

    private boolean canNotUpdateComment(String username) {
        CommentEntity commentPs = commentRepository.findLatestUpdateCommentByUsername(username)
            .orElseThrow(() -> new RuntimeException("최신 댓글을 찾을 수 없습니다."));

        Duration duration = Duration.between(commentPs.getUpdatedAt(), LocalDateTime.now());

        return duration.toMinutes() < 1;
    }


}
