package shokoku.board.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shokoku.board.view.repository.ArticleViewCountRepository;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

  private final ArticleViewCountRepository articleViewCountRepository;
  private final ArticleViewCountBackupProcessor articleViewCountBackupProcessor;
  private static final int BACK_UP_BACH_SIZE = 100;

  public Long increase(Long articleId, Long userId) {
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
