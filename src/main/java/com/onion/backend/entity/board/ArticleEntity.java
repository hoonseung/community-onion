package com.onion.backend.entity.board;

import com.onion.backend.entity.board.common.ArticleContent;
import com.onion.backend.entity.common.BaseEntity;
import com.onion.backend.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"article\"")
@Entity
public class ArticleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Embedded
    @Column(nullable = false, columnDefinition = "TEXT")
    private ArticleContent content;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity author;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardEntity board;


    public static ArticleEntity of(String title, String text, UserEntity author,
        BoardEntity board) {
        return new ArticleEntity(null,
            title, ArticleContent.of(text), author, board);
    }


}
