package kuke.board.comment.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kuke.board.comment.entity.Comment;
import kuke.board.comment.entity.CommentPath;
import kuke.board.comment.entity.CommentV2;
import kuke.board.common.snowflake.Snowflake;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class DataInitializerV2 {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TransactionTemplate transactionTemplate;

    Snowflake snowflake = new Snowflake();
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    static final int BULK_INSERT_SIZE = 2000;
    static final int EXECUTE_COUNT = 6000;

    @Test
    void initialize() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < EXECUTE_COUNT; i++) {
            final int threadId = i;
            executorService.submit(() -> {
                insert(BULK_INSERT_SIZE);
                latch.countDown();
                System.out.println("Thread " + threadId + " completed, latch.getCount() = " + latch.getCount());
            });
        }
        latch.await();
        executorService.shutdown();
    }

    void insert(int count){
        transactionTemplate.executeWithoutResult(status -> {
            for (int i = 0; i < count; i++) {
                CommentV2 comment = CommentV2.create(
                        snowflake.nextId(),
                        "content",
                        1L,
                        1L,
                        generateUniquePath()
                );
                entityManager.persist(comment);

                // 배치 크기마다 flush 수행
                if (i % 50 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        });
    }

    private static final String CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // UUID 기반으로 고유한 path 생성
    CommentPath generateUniquePath() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        StringBuilder path = new StringBuilder();

        // UUID의 첫 25자를 사용해서 path 생성 (varchar(25) 제한)
        for (int i = 0; i < Math.min(25, uuid.length()); i++) {
            char c = uuid.charAt(i);
            if (Character.isDigit(c)) {
                path.append(c);
            } else {
                // 16진수 문자를 CHARSET 인덱스로 변환
                int hexValue = Character.digit(c, 16);
                path.append(CHARSET.charAt(hexValue % CHARSET.length()));
            }
        }

        return CommentPath.create(path.toString());
    }
}
