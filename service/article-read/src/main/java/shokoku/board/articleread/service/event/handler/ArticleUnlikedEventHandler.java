package shokoku.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.articleread.repository.ArticleQueryModelRepository;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.ArticleLikedEventPayload;
import shokoku.board.common.event.payload.ArticleUnlikedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleUnlikedEventHandler implements EventHandler<ArticleUnlikedEventPayload>{
  private final ArticleQueryModelRepository articleQueryModelRepository;
  @Override
  public void handle(Event<ArticleUnlikedEventPayload> event) {
    articleQueryModelRepository.read(event.getPayload().getArticleId())
            .ifPresent(articleQueryModel -> {
              articleQueryModel.updatedBy(event.getPayload());
              articleQueryModelRepository.update(articleQueryModel);
            });
  }

  @Override
  public boolean supports(Event<ArticleUnlikedEventPayload> event) {
    return EventType.ARTICLE_UNLIKED == event.getType();
  }
}
