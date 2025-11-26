package shokoku.board.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import shokoku.board.comment.service.response.CommentPageResponse;
import shokoku.board.comment.service.response.CommentResponse;

import java.util.List;

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

  @Test
  void readAll() {
    CommentPageResponse response = restClient.get()
            .uri("/v1/comments?articleId=1&page=1&pageSize=10")
            .retrieve()
            .body(CommentPageResponse.class);

    System.out.println("response.getCommentCount() = " + response.getCommentCount());
    for (CommentResponse comment : response.getComments()) {
      if (!comment.getCommentId().equals(comment.getParentCommentId())) {
        System.out.print("\t");
      }
      System.out.println("comment.getCommentId() = " + comment.getCommentId());

      /*
        1번 페이지 수행 결과
        comment.getCommentId() = 252020464363741184
        	comment.getCommentId() = 252020464409878528
        comment.getCommentId() = 252020464363741185
        	comment.getCommentId() = 252020464409878529
        comment.getCommentId() = 252020464363741186
        	comment.getCommentId() = 252020464409878541
        comment.getCommentId() = 252020464363741187
        	comment.getCommentId() = 252020464409878531
        comment.getCommentId() = 252020464363741188
        	comment.getCommentId() = 252020464409878535
       */
    }
  }

  @Test
  void readAllInfiniteScroll() {
    List<CommentResponse> response1 = restClient.get()
            .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5")
            .retrieve()
            .body(new ParameterizedTypeReference<List<CommentResponse>>() {
            });

    System.out.println("firestPAge");
    for (CommentResponse comment : response1) {
      if (!comment.getCommentId().equals(comment.getParentCommentId())) {
        System.out.print("\t");
      }
      System.out.println("comment.getCommentId() = " + comment.getCommentId());
    }

    Long lastParentCommentId = response1.getLast().getParentCommentId();
    Long lastCommentId = response1.getLast().getCommentId();

    System.out.println("lastParentCommentId = " + lastParentCommentId);
    System.out.println("lastCommentId = " + lastCommentId);

    List<CommentResponse> response2 = restClient.get()
            .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5&lastParentCommentId=%s&lastCommentId=%s"
                    .formatted(lastParentCommentId, lastCommentId))
            .retrieve()
            .body(new ParameterizedTypeReference<List<CommentResponse>>() {
            });

    System.out.println("secondPage");
    for (CommentResponse comment : response2) {
      if (!comment.getCommentId().equals(comment.getParentCommentId())) {
        System.out.print("\t");
      }
      System.out.println("comment.getCommentId() = " + comment.getCommentId());
    }

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
