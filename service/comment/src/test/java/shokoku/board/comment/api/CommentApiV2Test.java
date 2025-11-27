package shokoku.board.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import shokoku.board.comment.service.response.CommentResponse;

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

  @Getter
  @AllArgsConstructor
  public static class CommentCreateRequestV2 {
    private Long articleId;
    private String content;
    private String parentPath;
    private Long writerId;
  }
}
