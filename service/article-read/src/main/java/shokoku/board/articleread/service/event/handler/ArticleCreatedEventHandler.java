package shokoku.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.articleread.repository.ArticleQueryModel;
import shokoku.board.articleread.repository.ArticleQueryModelRepository;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.ArticleCreatedEventPayload;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload>{
  private final ArticleQueryModelRepository articleQueryModelRepository;
  @Override
  public void handle(Event<ArticleCreatedEventPayload> event) {
    ArticleCreatedEventPayload payload = event.getPayload();
    articleQueryModelRepository.create(
            ArticleQueryModel.create(payload),
            Duration.ofDays(1)
    );
  }

  @Override
  public boolean supports(Event<ArticleCreatedEventPayload> event) {
    return EventType.ARTICLE_CREATED == event.getType();
  }
}
