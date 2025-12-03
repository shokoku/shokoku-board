package shokoku.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.articleread.repository.ArticleIdListRepository;
import shokoku.board.articleread.repository.ArticleQueryModelRepository;
import shokoku.board.articleread.repository.BoardArticleCountRepository;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.ArticleDeletedEventPayload;
import shokoku.board.common.event.payload.ArticleUpdatedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload>{
  private final ArticleQueryModelRepository articleQueryModelRepository;
  private final ArticleIdListRepository articleIdListRepository;
  private final BoardArticleCountRepository boardArticleCountRepository;
  @Override
  public void handle(Event<ArticleDeletedEventPayload> event) {
    ArticleDeletedEventPayload payload = event.getPayload();
    articleIdListRepository.delete(payload.getBoarId(), payload.getArticleId());
    articleQueryModelRepository.delete(payload.getArticleId());
    boardArticleCountRepository.createOrUpdate(payload.getBoarId(), payload.getBoardArticleCount());
  }

  @Override
  public boolean supports(Event<ArticleDeletedEventPayload> event) {
    return EventType.ARTICLE_DELETED == event.getType();
  }
}
