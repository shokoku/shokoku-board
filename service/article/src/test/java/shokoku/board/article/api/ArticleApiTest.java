package shokoku.board.article.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import shokoku.board.article.service.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleApiTest {

  RestClient restClient = RestClient.create("http://localhost:9000");

  private final List<Long> createdArticleIds = new ArrayList<>();

  @AfterEach
  void cleanup() {
    for (Long articleId : createdArticleIds) {
      try {
        delete(articleId);
      } catch (Exception ignored) {
      }
    }
    createdArticleIds.clear();
  }

  @Test
  void createTest() {
    ArticleResponse response = create(new ArticleCreateRequest("title", "content", 1L, 1L));
    assertNotNull(response);
    assertNotNull(response.getArticleId());
    assertEquals("title", response.getTitle());
    assertEquals("content", response.getContent());

    createdArticleIds.add(response.getArticleId());
    System.out.println("response = " + response);
  }

  ArticleResponse create(ArticleCreateRequest request) {
    return restClient.post()
            .uri("/v1/articles")
            .body(request)
            .retrieve()
            .body(ArticleResponse.class);
  }

  @Test
  void readTest() {
    ArticleResponse created = create(new ArticleCreateRequest("read test title", "read test content", 1L, 1L));
    createdArticleIds.add(created.getArticleId());

    ArticleResponse response = read(created.getArticleId());
    assertNotNull(response);
    assertEquals(created.getArticleId(), response.getArticleId());
    assertEquals("read test title", response.getTitle());
    assertEquals("read test content", response.getContent());

    System.out.println("response = " + response);
  }

  ArticleResponse read(Long articleId) {
    return restClient.get()
            .uri("/v1/articles/{articleId}", articleId)
            .retrieve()
            .body(ArticleResponse.class);
  }

  @Test
  void updatedTest() {
    ArticleResponse created = create(new ArticleCreateRequest("original title", "original content", 1L, 1L));
    createdArticleIds.add(created.getArticleId());

    update(created.getArticleId());

    ArticleResponse response = read(created.getArticleId());
    assertNotNull(response);
    assertEquals("title2", response.getTitle());
    assertEquals("content2", response.getContent());

    System.out.println("response = " + response);
  }

  void update(Long articleId){
    restClient.put()
            .uri("/v1/articles/{articleId}", articleId)
            .body(new ArticleUpdateRequest("title2", "content2"))
            .retrieve();
  }

  @Test
  void deleteTest() {
    ArticleResponse created = create(new ArticleCreateRequest("delete test title", "delete test content", 1L, 1L));
    Long articleId = created.getArticleId();

    delete(articleId);

    try {
      read(articleId);
      fail("삭제된 게시글을 조회할 수 없어야 합니다.");
    } catch (Exception e) {
      System.out.println("삭제 성공 확인");
    }
  }

  void delete(Long articleId) {
    restClient.delete()
            .uri("/v1/articles/{articleId}", articleId)
            .retrieve();
  }

  @Getter
  @AllArgsConstructor
  static class ArticleCreateRequest {
    private String title;
    private String content;
    private Long writerId;
    private Long boardId;
  }

  @Getter
  @AllArgsConstructor
  static class ArticleUpdateRequest {
    private String title;
    private String content;
  }
}
