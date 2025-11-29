package shokoku.board.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import shokoku.board.comment.service.response.CommentPageResponse;
import shokoku.board.comment.service.response.CommentResponse;

import java.util.List;

public class CommentApiV2Test {
  RestClient restClient = RestClient.create("http://localhost:9001");

  @Test
  void create() {
    CommentResponse response1 = create(new CommentCreateRequestV2(1L, "my comment", null, 1L));
    CommentResponse response2 = create(new CommentCreateRequestV2(1L, "my comment", response1.getPath(), 1L));
    CommentResponse response3 = create(new CommentCreateRequestV2(1L, "my comment", response2.getPath(), 1L));

    System.out.println("response1.getCommentId() = " + response1.getCommentId());
    System.out.println("\tresponse2.getCommentId() = " + response2.getCommentId());
    System.out.println("\t\tresponse3.getCommentId() = " + response3.getCommentId());

    System.out.println("response1.getPath() = " + response1.getPath());
    System.out.println("response1.getCommentId() = " + response1.getCommentId());
    System.out.println("\tresponse2.getPath() = " + response2.getPath());
    System.out.println("\tresponse2.getCommentId() = " + response2.getCommentId());
    System.out.println("\t\tresponse3.getPath() = " + response3.getPath());
    System.out.println("\t\tresponse3.getCommentId() = " + response3.getCommentId());

    /*
    response1.getCommentId() = 252383777890476032
  	response2.getCommentId() = 252383778184077312
		response3.getCommentId() = 252383778259574784
    response1.getPath() = 00004
    response1.getCommentId() = 252383777890476032
	  response2.getPath() = 0000400000
	  response2.getCommentId() = 252383778184077312
		response3.getPath() = 000040000000000
		response3.getCommentId() = 252383778259574784
     */

  }

  CommentResponse create(CommentCreateRequestV2 requst) {
    return restClient.post()
            .uri("/v2/comments")
            .body(requst)
            .retrieve()
            .body(CommentResponse.class);
  }

  @Test
  void read() {
    CommentResponse response = restClient.get()
            .uri("/v2/comments/{commentId}", 252383777890476032L)
            .retrieve()
            .body(CommentResponse.class);
    System.out.println("response = " + response);
  }

  @Test
  void delete() {
    restClient.delete()
            .uri("/v2/comments/{commentId}", 252383777890476032L)
            .retrieve();
  }

  @Test
  void readAll() {
    CommentPageResponse response = restClient.get()
            .uri("/v2/comments?articleId=1&pageSize=10&page=50000")
            .retrieve()
            .body(CommentPageResponse.class);

    System.out.println("response.getCommentCount() = " + response.getCommentCount());
    for (CommentResponse comment : response.getComments()) {
      System.out.println("comment.getCommentId() = " + comment.getCommentId());
    }

    /*
    response.getCommentCount() = 101
    comment.getCommentId() = 252383135419572224
    comment.getCommentId() = 252383136082272256
    comment.getCommentId() = 252383136161964032
    comment.getCommentId() = 252383269557608448
    comment.getCommentId() = 252383269771517952
    comment.getCommentId() = 252383269826043904
    comment.getCommentId() = 252383414537920512
    comment.getCommentId() = 252383414760218624
    comment.getCommentId() = 252383414802161664
    comment.getCommentId() = 252383642146021376
     */
  }

  @Test
  void readALlInfiniteScroll() {
    List<CommentResponse> response1 = restClient.get()
            .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5")
            .retrieve()
            .body(new ParameterizedTypeReference<List<CommentResponse>>() {
            });
    System.out.println("firestPage");
    for (CommentResponse response : response1) {
      System.out.println("response.getCommentId() = " + response.getCommentId());
    }

    String lastPAth = response1.getLast().getPath();
    List<CommentResponse> response2= restClient.get()
            .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5&lastPath=%s".formatted(lastPAth))
            .retrieve()
            .body(new ParameterizedTypeReference<List<CommentResponse>>() {
            });
    System.out.println("secondPage");
    for (CommentResponse response : response2) {
      System.out.println("response.getCommentId() = " + response.getCommentId());
    }
  }

  @Test
  void countTest() {
    CommentResponse commentResponse = create(new CommentCreateRequestV2(2L, "my comment", null, 1L));

    Long count1 = restClient.get()
            .uri("/v2/comments/articles/{articleId}/count", 2L)
            .retrieve()
            .body(Long.class);

    System.out.println("count1 = " + count1);

    restClient.delete()
            .uri("/v2/comments/{commentId}", commentResponse.getCommentId())
            .retrieve();

    Long count2= restClient.get()
            .uri("/v2/comments/articles/{articleId}/count", 2L)
            .retrieve()
            .body(Long.class);
    System.out.println("count2 = " + count2);
  }

  @Getter
  @AllArgsConstructor
  public static class CommentCreateRequestV2 {
    private Long articleId;
    private String content;
    private String parentPath;
    private Long writerId;
  }
}
