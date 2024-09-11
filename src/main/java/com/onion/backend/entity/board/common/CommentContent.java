package com.onion.backend.entity.board.common;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CommentContent {

    private String text;


}
