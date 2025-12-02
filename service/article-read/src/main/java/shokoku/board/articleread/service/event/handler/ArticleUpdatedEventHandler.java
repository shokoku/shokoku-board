package shokoku.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.articleread.repository.ArticleQueryModel;
import shokoku.board.articleread.repository.ArticleQueryModelRepository;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.ArticleCreatedEventPayload;
import shokoku.board.common.event.payload.ArticleUpdatedEventPayload;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleUpdatedEventHandler implements EventHandler<ArticleUpdatedEventPayload>{
  private final ArticleQueryModelRepository articleQueryModelRepository;
  @Override
  public void handle(Event<ArticleUpdatedEventPayload> event) {
   articleQueryModelRepository.read(event.getPayload().getArticleId())
           .ifPresent(articleQueryModel -> {
             articleQueryModel.updatedBy(event.getPayload());
             articleQueryModelRepository.update(articleQueryModel);
           });
  }

  @Override
  public boolean supports(Event<ArticleUpdatedEventPayload> event) {
    return EventType.ARTICLE_UPDATED == event.getType();
  }
}
