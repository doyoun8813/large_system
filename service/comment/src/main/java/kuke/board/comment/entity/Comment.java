package kuke.board.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "comment")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    private Long commentId;
    private String content;
    private Long parentCommentId;
    private Long articleId;
    private Long writerId;
    private Boolean deleted;
    private LocalDateTime createdAt;

    public static Comment create(Long commentId, String content, Long parentCommentId, Long articleId, Long writerId) {
        Comment comment = new Comment();
        comment.commentId = commentId;
        comment.content = content;
        comment.parentCommentId = parentCommentId == null ? commentId : parentCommentId;
        comment.articleId = articleId;
        comment.writerId = writerId;
        comment.deleted = false;
        comment.createdAt = LocalDateTime.now();
        return comment;
    }

    public boolean isRoot() {
        // TODO 왜 이거 썼지?
        // return parentCommentId.longValue() == commentId;
        return Objects.equals(commentId, parentCommentId);

        //longValue()를 사용한 이유는 Long 타입의 박싱된 객체(래퍼 클래스)를 기본형 long으로 변환하여 비교하려는 의도입니다. 하지만 이 코드에서는 불필요하거나 혼동을 줄 수 있는 사용입니다.
        //parentCommentId와 commentId는 둘 다 Long (객체 타입)입니다.
        //
        //== 연산자는 기본형에서는 값 비교, 객체에서는 참조 비교(주소 비교) 를 합니다.
        //
        //parentCommentId.longValue()는 Long 객체를 long 기본형으로 변환합니다.
        //
        //commentId는 여전히 Long이기 때문에 == 연산에서 commentId도 자동 언박싱되어 long으로 변환되고, 두 long 값이 비교됩니다.

        // return parentCommentId.equals(commentId);
        // return parentCommentId == commentId;

        // longValue()를 쓴 이유: 명시적으로 언박싱해서 값 비교를 하려는 의도.
        //
        //하지만 불필요합니다. Long끼리의 값 비교는 equals()를 쓰는 게 더 안전하고 가독성도 좋습니다.
        //
        //null 가능성이 있을 경우에는 equals() 사용이 더 안전:

        // return commentId != null && commentId.equals(parentCommentId);
        // 또는 Java 17 이후라면 Objects.equals(commentId, parentCommentId) 도 깔끔한 방법입니다.
    }

    public void delete() {
        deleted = true;
    }
}
