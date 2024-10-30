package com.onion.backend.entity.board.common;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CommentContent {

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String text;


}
