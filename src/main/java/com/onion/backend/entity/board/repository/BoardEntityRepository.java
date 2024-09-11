package com.onion.backend.entity.board.repository;

import com.onion.backend.entity.board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardEntityRepository extends JpaRepository<BoardEntity, Long> {

}
