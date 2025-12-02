package shokoku.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.CommentCreatedEventPayload;
import shokoku.board.hotarticle.repository.ArticleCommentCountRepository;
import shokoku.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload>{
  private final ArticleCommentCountRepository articleCommentCountRepository;
  @Override
  public void handle(Event<CommentCreatedEventPayload> event) {
    CommentCreatedEventPayload payload = event.getPayload();
    articleCommentCountRepository.createOrUpdate(
            payload.getArticleId(),
            payload.getArticleCommentCount(),
            TimeCalculatorUtils.calculateDurationToMidnight()
    );
  }

  @Override
  public boolean supports(Event<CommentCreatedEventPayload> event) {
    return event.getType() == EventType.COMMENT_CREATED;
  }

  @Override
  public Long findArticleId(Event<CommentCreatedEventPayload> event) {
    return event.getPayload().getArticleId();
  }
}
