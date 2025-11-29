package shokoku.board.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shokoku.board.view.entity.ArticleViewCount;
import shokoku.board.view.repository.ArticleViewCountBackupRepository;

@Component
@RequiredArgsConstructor
public class ArticleViewCountBackupProcessor {

  private final ArticleViewCountBackupRepository articleViewCountBackupRepository;

  @Transactional
  public void backup(Long articleId, Long viewCount) {
    int result = articleViewCountBackupRepository.updateViewCount(articleId, viewCount);
    if (result == 0) {
      articleViewCountBackupRepository.findById(articleId)
              .ifPresentOrElse(ignored -> {},
                      () -> articleViewCountBackupRepository.save(ArticleViewCount.init(articleId, viewCount)));
    }
  }

}
