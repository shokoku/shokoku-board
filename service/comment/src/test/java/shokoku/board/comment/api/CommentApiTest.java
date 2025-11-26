package shokoku.board.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import shokoku.board.comment.service.response.CommentResponse;

public class CommentApiTest {
  RestClient restClient = RestClient.create("http://localhost:9001");

  @Test
  void create() {
    CommentResponse response1 = createComment(new CommentCreateRequest(1L, "my comment1", null, 1L));
    CommentResponse response2 = createComment(new CommentCreateRequest(1L, "my comment2", response1.getCommentId(), 1L));
    CommentResponse response3 = createComment(new CommentCreateRequest(1L, "my comment3", response1.getCommentId(), 1L));

    System.out.printf("commentId=%s%n", response1.getCommentId());
    System.out.printf("commentId=%s%n", response2.getCommentId());
    System.out.printf("commentId=%s%n", response3.getCommentId());

//    commentId=252016611461369856
//    commentId=252016611922743296
//    commentId=252016611977269248
  }

  CommentResponse createComment(CommentCreateRequest requst) {
    return restClient.post()
            .uri("/v1/comments")
            .body(requst)
            .retrieve()
            .body(CommentResponse.class);
  }

  @Test
  void read() {
    CommentResponse response = restClient.get()
            .uri("/v1/comments/{commentId}", 252016611461369856L)
            .retrieve()
            .body(CommentResponse.class);
    System.out.println("response = " + response);
  }

  @Test
  void delete() {
    //    commentId=252016611461369856 - x
    //    commentId=252016611922743296 - x
    //    commentId=252016611977269248 - x

    restClient.delete()
            .uri("/v1/comments/{commentId}", 252016611977269248L)
            .retrieve();
  }

  @Getter
  @AllArgsConstructor
  public static class CommentCreateRequest {
    private Long articleId;
    private String content;
    private Long parentContentId;
    private Long writerId;
  }

}
