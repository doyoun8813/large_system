package kuke.board.articleread.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewClient {
    private RestClient restClient;
    @Value("${endpoints.kuke-view-like-service.url}")
    private String viewServiceUrl;

    @PostConstruct
    public void initRestClient() {
        restClient = RestClient.create(viewServiceUrl);
    }

    public long read(Long articleId) {
        try {
            return restClient.get()
                    .uri("/v1/article-views1/articles/{articleId}/count", articleId)
                    .retrieve()
                    .body(Long.class);
        } catch (Exception e) {
            log.error("[ViewClient.read] articleId={}", articleId, e);
            return 0;
        }
    }

}
