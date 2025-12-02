package shokoku.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.articleread.repository.ArticleQueryModelRepository;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.ArticleDeletedEventPayload;
import shokoku.board.common.event.payload.ArticleUpdatedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload>{
  private final ArticleQueryModelRepository articleQueryModelRepository;
  @Override
  public void handle(Event<ArticleDeletedEventPayload> event) {
    ArticleDeletedEventPayload payload = event.getPayload();
    articleQueryModelRepository.delete(payload.getArticleId());
  }

  @Override
  public boolean supports(Event<ArticleDeletedEventPayload> event) {
    return EventType.ARTICLE_DELETED == event.getType();
  }
}
