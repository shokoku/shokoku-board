package shokoku.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.ArticleViewedEventPayload;
import shokoku.board.hotarticle.repository.ArticleViewCountRepository;
import shokoku.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleViewEventHandler implements EventHandler<ArticleViewedEventPayload>{
  private final ArticleViewCountRepository articleViewCountRepository;
  @Override
  public void handle(Event<ArticleViewedEventPayload> event) {
    ArticleViewedEventPayload payload = event.getPayload();
    articleViewCountRepository.createOrUpdate(
            payload.getArticleId(),
            payload.getArticleViewCount(),
            TimeCalculatorUtils.calculateDurationToMidnight()
    );
  }

  @Override
  public boolean support(Event<ArticleViewedEventPayload> event) {
    return event.getType() == EventType.ARTICLE_VIEWED;
  }

  @Override
  public Long findArticleId(Event<ArticleViewedEventPayload> event) {
    return event.getPayload().getArticleId();
  }
}
