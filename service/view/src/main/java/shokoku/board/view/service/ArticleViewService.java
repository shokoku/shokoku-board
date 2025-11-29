package shokoku.board.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shokoku.board.view.repository.ArticleViewCountRepository;
import shokoku.board.view.repository.ArticleViewDistributedLockRepository;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

  private final ArticleViewCountRepository articleViewCountRepository;
  private final ArticleViewCountBackupProcessor articleViewCountBackupProcessor;
  private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
  private static final int BACK_UP_BACH_SIZE = 100;
  private static final Duration TTL = Duration.ofMinutes(10);

  public Long increase(Long articleId, Long userId) {
    if (!articleViewDistributedLockRepository.lock(articleId, userId, TTL)) {
      return count(articleId);
    }

    Long count = articleViewCountRepository.increase(articleId);
    if (count % BACK_UP_BACH_SIZE == 0) {
      articleViewCountBackupProcessor.backup(articleId, count);
    }
    return count;
  }

  public Long count(Long articleId) {
    return articleViewCountRepository.read(articleId);
  }
}
