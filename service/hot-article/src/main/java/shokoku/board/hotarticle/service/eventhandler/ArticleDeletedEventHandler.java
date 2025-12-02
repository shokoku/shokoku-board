package shokoku.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.ArticleDeletedEventPayload;
import shokoku.board.hotarticle.repository.ArticleCreatedTimeRepository;
import shokoku.board.hotarticle.repository.HotArticleListRepository;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
  private final HotArticleListRepository hotArticleListRepository;
  private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

  @Override
  public void handle(Event<ArticleDeletedEventPayload> event) {
    ArticleDeletedEventPayload payload = event.getPayload();
    articleCreatedTimeRepository.delete(payload.getArticleId());
    hotArticleListRepository.remove(payload.getArticleId(), payload.getCreatedAt());
  }

  @Override
  public boolean supports(Event<ArticleDeletedEventPayload> event) {
    return EventType.ARTICLE_DELETED == event.getType();
  }

  @Override
  public Long findArticleId(Event<ArticleDeletedEventPayload> event) {
    return event.getPayload().getArticleId();
  }
}
