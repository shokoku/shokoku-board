package shokoku.board.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shokoku.board.comment.service.CommentService;
import shokoku.board.comment.service.CommentServiceV2;
import shokoku.board.comment.service.request.CommentCreateRequest;
import shokoku.board.comment.service.request.CommentCreateRequestV2;
import shokoku.board.comment.service.response.CommentPageResponse;
import shokoku.board.comment.service.response.CommentResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentControllerV2 {

  private final CommentServiceV2 commentService;

  @GetMapping("/v2/comments/{commentId}")
  public CommentResponse read(@PathVariable Long commentId) {
    return commentService.read(commentId);
  }

  @PostMapping("/v2/comments")
  public CommentResponse create(@RequestBody CommentCreateRequestV2 request) {
    return commentService.create(request);
  }

  @DeleteMapping("/v2/comments/{commentId}")
  public void delete(@PathVariable Long commentId) {
    commentService.delete(commentId);
  }

}
