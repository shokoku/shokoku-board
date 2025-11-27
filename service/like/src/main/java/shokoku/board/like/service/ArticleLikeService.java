package shokoku.board.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shokoku.board.common.snowflake.Snowflake;
import shokoku.board.like.entity.ArticleLike;
import shokoku.board.like.repository.ArticleLikeRepository;
import shokoku.board.like.service.response.ArticleLikeResponse;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

  private final Snowflake snowflake = new Snowflake();
  private final ArticleLikeRepository articleLikeRepository;

  public ArticleLikeResponse read(Long articleId, Long userId) {
    return articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
            .map(ArticleLikeResponse::from)
            .orElseThrow();
  }

  @Transactional
  public void like(Long articleId, Long userId) {
    articleLikeRepository.save(
            ArticleLike.create(
                    snowflake.nextId(),
                    articleId,
                    userId
            )
    );
  }

  @Transactional
  public void unLike(Long articleId, Long userId) {
    articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
            .ifPresent(articleLikeRepository::delete);

  }
}
