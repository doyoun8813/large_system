package kuke.board.like.repository;

import jakarta.persistence.LockModeType;
import kuke.board.like.entity.ArticleLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long> {

    // select ... for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    // TODO
    /*
    PESSIMISTIC_WRITE (배타적 락):
    한 번에 하나의 트랜잭션만 특정 데이터에 대해 잠금 상태를 유지하며, 다른 트랜잭션이 해당 데이터에 접근하거나 변경하는 것을 막습니다.
    PESSIMISTIC_READ (공유 락):
    여러 트랜잭션이 특정 데이터를 동시에 읽을 수 있지만, 변경하는 것을 막습니다.
    OPTIMISTIC_LOCK (낙관적 락):
    버전 정보를 사용하여 데이터 변경 충돌을 감지하고 해결하는 방식으로, 데이터베이스에 직접 잠금 설정 없이 데이터 무결성을 유지합니다. */
    Optional<ArticleLikeCount> findLockedByArticleId(Long articleId);

    @Query(
            value="update article_like_count set like_count = like_count + 1 " +
                  "where article_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int increase(@Param("articleId") Long articleId);

    @Query(
            value="update article_like_count set like_count = like_count - 1 " +
                    "where article_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int decrease(@Param("articleId") Long articleId);


}
