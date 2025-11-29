package shokoku.board.like.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import shokoku.board.like.service.response.ArticleLikeResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LikeApiTest {

  RestClient restClient = RestClient.create("http://localhost:9002");

  @Test
  void likeAndUnlikeTest() {
    Long articleId = 9999L;

    like(articleId, 1L, "pessimistic-lock1");
    like(articleId, 2L, "pessimistic-lock1");
    like(articleId, 3L, "pessimistic-lock1");

    ArticleLikeResponse response1 = read(articleId, 1L);
    ArticleLikeResponse response2 = read(articleId, 2L);
    ArticleLikeResponse response3 = read(articleId, 3L);

    System.out.println("response1 = " + response1);
    System.out.println("response2 = " + response2);
    System.out.println("response3 = " + response3);

    unLike(articleId, 1L);
    unLike(articleId, 2L);
    unLike(articleId, 3L);



  }

  void like(Long articleId, Long userId, String lockType) {
    restClient.post()
            .uri("/v1/article-likes/articles/{articleId}/users/{userId}/" + lockType, articleId, userId)
            .retrieve();
  }

  void unLike(Long articleId, Long userId) {
    restClient.delete()
            .uri("/v1/article-likes/articles/{articleId}/users/{userId}", articleId, userId)
            .retrieve();
  }

  ArticleLikeResponse read(Long articleId, Long userId) {
    return restClient.get()
            .uri("/v1/article-likes/articles/{articleId}/users/{userId}", articleId, userId)
            .retrieve()
            .body(ArticleLikeResponse.class);
  }

  @Test
  void likePerformanceTest() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(100);
    likePerformanceTest(executorService, 1111L, "pessimistic-lock1");
    likePerformanceTest(executorService, 1111L, "pessimistic-lock2");
    likePerformanceTest(executorService, 1111L, "optimistic-Lock");
  }

  void likePerformanceTest(ExecutorService executorService, Long articleId, String lockType) throws InterruptedException {

    CountDownLatch latch = new CountDownLatch(3000);
    System.out.println(lockType + " start");

    like(articleId, 1L, lockType);

    long start = System.nanoTime();
    for (int i = 0; i < 3000; i++) {
      long userId = i + 2;
      executorService.submit(() -> {
        like(articleId, userId, lockType);
        latch.countDown();
      });
    }

    latch.await();

    long end = System.nanoTime();

    System.out.println("lockType = " + lockType + ", time = " + (end - start) / 1000000 + "ms");

    Long count = restClient.get().uri("/v1/article-likes/articles/{articleId}/count", articleId)
            .retrieve()
            .body(Long.class);

    System.out.println("count = " + count);


  }
}

