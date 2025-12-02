package shokoku.board.hotarticle.service.eventhandler;

import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventPayload;

public interface EventHandler <T extends EventPayload>{
  void handle(Event<T> event);
  boolean supports(Event<T> event);
  Long findArticleId(Event<T> event);

}
