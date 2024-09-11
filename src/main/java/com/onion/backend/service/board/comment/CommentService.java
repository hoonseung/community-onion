package com.onion.backend.service.board.comment;

import com.onion.backend.dto.comment.Comment;
import com.onion.backend.dto.comment.CreateCommentReplyRequest;
import com.onion.backend.dto.comment.CreateCommentRequest;
import com.onion.backend.dto.comment.UpdateCommentRequest;
import com.onion.backend.dto.common.exception.TimeRateLimitException;
import com.onion.backend.entity.board.ArticleEntity;
import com.onion.backend.entity.board.CommentEntity;
import com.onion.backend.entity.board.repository.CommentRepository;
import com.onion.backend.service.board.article.ArticleService;
import com.onion.backend.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
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
            CommentEntity.of(request.content(), userService.getUser(username),
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
            request.content(),
            userService.getUser(username),
            articleService.getArticleEntity(boardId, articleId)
        );

        commentPs.addComment(commentEntity);

        return Comment.from(commentRepository.save(commentEntity));
    }

    @Async
    public CompletableFuture<List<Comment>> getAsyncWithComments(Long articleId) {
        return CompletableFuture.completedFuture(
            commentRepository.findAllByArticleId(articleId).stream()
                .map(Comment::from)
                .toList()
        );
    }


    private boolean canNotWriteComment(String username) {
        Optional<CommentEntity> commentPs = commentRepository.findLatestCreateCommentByUsername(
            username);
        if (commentPs.isPresent()) {
            Duration duration = Duration.between(commentPs.get().getCreatedAt(),
                LocalDateTime.now());
            return duration.toMinutes() < 1;
        }
        return false;
    }

    private boolean canNotUpdateComment(String username) {
        CommentEntity commentPs = commentRepository.findLatestUpdateCommentByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("최신 댓글을 찾을 수 없습니다."));

        Duration duration = Duration.between(commentPs.getUpdatedAt(), LocalDateTime.now());
        return duration.toMinutes() < 1;
    }

    private boolean canNotDeleteComment(String username) {
        Optional<CommentEntity> commentPs = commentRepository.findLatestDeleteCommentByUsername(
            username);
        if (commentPs.isPresent()) {
            Duration duration = Duration.between(commentPs.get().getUpdatedAt(),
                LocalDateTime.now());
            return duration.toMinutes() < 1;
        }
        return false;
    }


    @Transactional
    public Long editComment(Long boardId, Long articleId, Long commentId, String username,
        UpdateCommentRequest request) {
        if (canNotUpdateComment(username)) {
            throw new TimeRateLimitException("1분 이내에 수정한 댓글이 있습니다.");
        }
        ArticleEntity articlePs = articleService.getArticleEntity(boardId, articleId);
        commentRepository.findById(commentId)
            .filter(comment -> comment.getArticle().getId().equals(articlePs.getId()))
            .ifPresentOrElse(comment -> {
                    comment.getAuthor().checkUsername(username);
                    comment.edit(request.text());
                },
                () -> {
                    throw new EntityNotFoundException(
                        String.format("%d 번 게시글에 댓글이 존재하지 않습니다.", articlePs.getId()));
                });

        return commentId;
    }


    @Transactional
    public void deleteComment(Long boardId, Long articleId, Long commentId, String username) {
        if (canNotDeleteComment(username)) {
            throw new TimeRateLimitException("1분 이내에 삭제한 댓글이 있습니다.");
        }
        ArticleEntity articlePs = articleService.getArticleEntity(boardId, articleId);
        CommentEntity commentPs = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        if (commentPs.getArticle().getId().equals(articlePs.getId())) {
            commentPs.getAuthor().checkUsername(username);
            commentRepository.delete(commentPs);
        } else {
            throw new EntityNotFoundException(
                String.format("%d 번 게시글에 댓글이 존재하지 않습니다.", articlePs.getId()));
        }
    }
}
