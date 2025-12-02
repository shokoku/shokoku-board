package shokoku.board.articleread.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import shokoku.board.articleread.service.response.ArticleReadResponse;

public class ArticleReadApiTest {
  RestClient restClient = RestClient.create("http://localhost:9005");

  @Test
  void readTest() {
    ArticleReadResponse response = restClient.get()
            .uri("/v1/articles/{articleId}", 253025779343413248L)
            .retrieve()
            .body(ArticleReadResponse.class);
    System.out.println("response = " + response);
  }
}
