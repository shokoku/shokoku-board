package shokoku.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.articleread.repository.ArticleQueryModelRepository;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.CommentCreatedEventPayload;
import shokoku.board.common.event.payload.CommentDeletedEventPayload;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload>{
  private final ArticleQueryModelRepository articleQueryModelRepository;
  @Override
  public void handle(Event<CommentDeletedEventPayload> event) {
    articleQueryModelRepository.read(event.getPayload().getArticleId())
            .ifPresent(articleQueryModel -> {
              articleQueryModel.updatedBy(event.getPayload());
              articleQueryModelRepository.update(articleQueryModel);
            });
  }

  @Override
  public boolean supports(Event<CommentDeletedEventPayload> event) {
    return EventType.COMMENT_DELETED == event.getType();
  }
}
