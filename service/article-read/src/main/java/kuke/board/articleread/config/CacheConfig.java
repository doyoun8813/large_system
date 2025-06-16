package kuke.board.articleread.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        System.out.println("RedisConnectionFactory type: " + connectionFactory.getClass());

        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(
                        Map.of(
                                // TODO 왜 캐시 TTL 1초만 주는거지?
                                // TTL이 1초인 이유는 최신 데이터가 중요하기 때문 (거의 실시간에 가까운 응답이 요구될 때 사용).
                                // 그럼 캐시에 안담고 그냥 조회하면 되는거 아닌가...
                                "articleViewCount", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(1))
                        )
                )
                .build();
    }
}
