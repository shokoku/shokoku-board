package shokoku.board.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.ArticleViewedEventPayload;
import shokoku.board.common.outboxmessagerelay.OutboxEventPublisher;
import shokoku.board.view.entity.ArticleViewCount;
import shokoku.board.view.repository.ArticleViewCountBackupRepository;

@Component
@RequiredArgsConstructor
public class ArticleViewCountBackupProcessor {
  private final OutboxEventPublisher outboxEventPublisher;
  private final ArticleViewCountBackupRepository articleViewCountBackupRepository;

  @Transactional
  public void backup(Long articleId, Long viewCount) {
    int result = articleViewCountBackupRepository.updateViewCount(articleId, viewCount);
    if (result == 0) {
      articleViewCountBackupRepository.findById(articleId)
              .ifPresentOrElse(ignored -> {},
                      () -> articleViewCountBackupRepository.save(ArticleViewCount.init(articleId, viewCount)));
    }

    outboxEventPublisher.publish(
            EventType.ARTICLE_VIEWED,
            ArticleViewedEventPayload.builder()
                    .articleId(articleId)
                    .articleViewCount(viewCount)
                    .build(),
            articleId
    );
  }

}
