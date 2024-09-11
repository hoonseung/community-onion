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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE article SET is_deleted = 1, updated_at = now() WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "\"article\"", indexes = {
    @Index(name = "idx_article_board_id", columnList = "id, board_id")})
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    private List<CommentEntity> comments;

    @Column(nullable = false)
    private boolean isDeleted;


    public static ArticleEntity of(String title, String text, UserEntity author,
        BoardEntity board) {
        return new ArticleEntity(null,
            title, ArticleContent.of(text), author, board, new ArrayList<>(), false);
    }


    public void edit(String title, String text) {
        this.title = StringUtils.hasText(title) ? title : this.title;
        this.content = StringUtils.hasText(text) ? ArticleContent.of(text) : this.content;
    }
}
