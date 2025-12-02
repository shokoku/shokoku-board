package shokoku.board.hotarticle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventPayload;
import shokoku.board.common.event.EventType;
import shokoku.board.hotarticle.client.ArticleClient;
import shokoku.board.hotarticle.repository.HotArticleListRepository;
import shokoku.board.hotarticle.service.eventhandler.EventHandler;
import shokoku.board.hotarticle.service.response.HotArticleResponse;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotArticleService {

  private final ArticleClient articleClient;
  private final List<EventHandler> eventHandlers;
  private final HotArticleScoreUpdater hotArticleScoreUpdater;
  private final HotArticleListRepository hotArticleListRepository;

  public void handleEvent(Event<EventPayload> event) {
    EventHandler<EventPayload> eventHandler = findEventHandler(event);
    if (eventHandler == null) {
      return;
    }

    if (isArticleCreateOrDelete(event)) {
      eventHandler.handle(event);
    } else {
      hotArticleScoreUpdater.update(event, eventHandler);
    }
  }

  private EventHandler<EventPayload> findEventHandler(Event<EventPayload> event) {
    return eventHandlers.stream()
            .filter(eventHandler -> eventHandler.support(event))
            .findAny()
            .orElse(null);
  }

  private boolean isArticleCreateOrDelete(Event<EventPayload> event) {
    return EventType.ARTICLE_CREATED == event.getType() || EventType.ARTICLE_DELETED == event.getType();
  }

  public List<HotArticleResponse> readAll(String dataStr) {
    hotArticleListRepository.readAll(dataStr).stream()
            .map(articleClient::read)
            .filter(Objects::nonNull)
            .map(HotArticleResponse::from)
            .toList();

    return null;
  }
}
