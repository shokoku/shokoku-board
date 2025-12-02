package shokoku.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.articleread.repository.ArticleQueryModelRepository;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.ArticleDeletedEventPayload;
import shokoku.board.common.event.payload.CommentCreatedEventPayload;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload>{
  private final ArticleQueryModelRepository articleQueryModelRepository;
  @Override
  public void handle(Event<CommentCreatedEventPayload> event) {
    articleQueryModelRepository.read(event.getPayload().getArticleId())
            .ifPresent(articleQueryModel -> {
              articleQueryModel.updatedBy(event.getPayload());
              articleQueryModelRepository.update(articleQueryModel);
            });
  }

  @Override
  public boolean supports(Event<CommentCreatedEventPayload> event) {
    return EventType.COMMENT_CREATED == event.getType();
  }
}
