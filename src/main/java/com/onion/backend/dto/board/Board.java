package com.onion.backend.dto.board;

import com.onion.backend.entity.board.BoardEntity;
import java.time.LocalDateTime;

public record Board(
    Long id,
    String title,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static Board from(BoardEntity entity) {
        return new Board(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

}
