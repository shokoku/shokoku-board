package shokoku.board.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shokoku.board.like.service.ArticleLikeService;
import shokoku.board.like.service.response.ArticleLikeResponse;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {

  private final ArticleLikeService articleLikeService;

  @GetMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
  public ArticleLikeResponse read(
          @PathVariable Long articleId,
          @PathVariable Long userId) {
    return articleLikeService.read(articleId, userId);
  }

  @GetMapping("/v1/article-likes/articles/{articleId}/count")
  public Long count(
          @PathVariable Long articleId){
    return articleLikeService.count(articleId);
  }

  @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock1")
  public void likePessimisticLock1(
          @PathVariable Long articleId,
          @PathVariable Long userId) {
    articleLikeService.likePessimisticLock1(articleId, userId);
  }

  @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock1")
  public void unLikePessimisticLock1(
          @PathVariable Long articleId,
          @PathVariable Long userId) {
    articleLikeService.unLikePessimisticLock1(articleId, userId);
  }

  @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock2")
  public void likePessimisticLock2(
          @PathVariable Long articleId,
          @PathVariable Long userId) {
    articleLikeService.likePessimisticLock2(articleId, userId);
  }

  @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock2")
  public void unLikePessimisticLock2(
          @PathVariable Long articleId,
          @PathVariable Long userId) {
    articleLikeService.unLikePessimisticLock2(articleId, userId);
  }

  @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock")
  public void likeOptimisticLock(
          @PathVariable Long articleId,
          @PathVariable Long userId) {
    articleLikeService.likeOptimisticLock(articleId, userId);
  }

  @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock")
  public void unLikeOptimisticLock(
          @PathVariable Long articleId,
          @PathVariable Long userId) {
    articleLikeService.unLikeOptimisticLock(articleId, userId);
  }
}
