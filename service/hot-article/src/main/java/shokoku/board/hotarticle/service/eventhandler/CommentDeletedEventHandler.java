package shokoku.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.CommentDeletedEventPayload;
import shokoku.board.hotarticle.repository.ArticleCommentCountRepository;
import shokoku.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload> {
  private final ArticleCommentCountRepository articleCommentCountRepository;
  @Override
  public void handle(Event<CommentDeletedEventPayload> event) {
    CommentDeletedEventPayload payload = event.getPayload();
    articleCommentCountRepository.createOrUpdate(
            payload.getArticleId(),
            payload.getArticleCommentCount(),
            TimeCalculatorUtils.calculateDurationToMidnight()
    );
  }

  @Override
  public boolean support(Event<CommentDeletedEventPayload> event) {
    return event.getType() == EventType.COMMENT_DELETED;
  }

  @Override
  public Long findArticleId(Event<CommentDeletedEventPayload> event) {
    return event.getPayload().getArticleId();
  }
}
