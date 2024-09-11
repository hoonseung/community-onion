package com.onion.backend.entity.board;

import com.onion.backend.entity.board.common.CommentContent;
import com.onion.backend.entity.common.BaseEntity;
import com.onion.backend.entity.user.UserEntity;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE comment SET is_deleted = 1, updated_at = now() WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "\"comment\"")
@Entity
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Column(nullable = false, columnDefinition = "TEXT")
    private CommentContent content;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity author;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private ArticleEntity article;

    @Setter
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentEntity parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> childComments;

    @Column(nullable = false)
    private boolean isDeleted;


    public static CommentEntity of(String text, UserEntity author,
        ArticleEntity article) {
        return new CommentEntity(null,
            CommentContent.of(text), author, article, null, new ArrayList<>(), false);
    }


    public void edit(String text) {
        this.content = StringUtils.hasText(text) ? CommentContent.of(text) : this.content;
    }

    public void addComment(CommentEntity commentEntity) {
        this.childComments.add(commentEntity);
        commentEntity.setParentComment(this);
    }


}
