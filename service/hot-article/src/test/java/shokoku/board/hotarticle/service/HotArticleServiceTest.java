package shokoku.board.hotarticle.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventType;
import shokoku.board.hotarticle.client.ArticleClient;
import shokoku.board.hotarticle.repository.HotArticleListRepository;
import shokoku.board.hotarticle.service.eventhandler.EventHandler;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotArticleServiceTest {

  @InjectMocks
  HotArticleService hotArticleService;

  @Mock
  ArticleClient articleClient;
  @Mock
  List<EventHandler> eventHandlers;

  @Mock
  HotArticleScoreUpdater hotArticleScoreUpdater;

  @Mock
  HotArticleListRepository hotArticleListRepository;

  @Test
  void handleEventIfEventHandlerNotFoundTest() {
    Event event = mock(Event.class);
    EventHandler eventHandler = mock(EventHandler.class);
    given(eventHandler.supports(event)).willReturn(false);
    given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

    hotArticleService.handleEvent(event);

    verify(eventHandler, never()).handle(event);
    verify(hotArticleScoreUpdater, never()).update(event, eventHandler);
  }

  @Test
  void handleEventIfArticleCreatedEvents() {
    Event event = mock(Event.class);
    given(event.getType()).willReturn(EventType.ARTICLE_CREATED);

    EventHandler eventHandler = mock(EventHandler.class);
    given(eventHandler.supports(event)).willReturn(true);
    given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

    hotArticleService.handleEvent(event);

    verify(eventHandler).handle(event);
    verify(hotArticleScoreUpdater, never()).update(event, eventHandler);
  }

  @Test
  void handleEventIfArticleDeletedEvents() {
    Event event = mock(Event.class);
    given(event.getType()).willReturn(EventType.ARTICLE_DELETED);

    EventHandler eventHandler = mock(EventHandler.class);
    given(eventHandler.supports(event)).willReturn(true);
    given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

    hotArticleService.handleEvent(event);

    verify(eventHandler).handle(event);
    verify(hotArticleScoreUpdater, never()).update(event, eventHandler);
  }

  @Test
  void handleEventIfScoreUpdatableEvents() {
    Event event = mock(Event.class);
    given(event.getType()).willReturn(mock(EventType.class));

    EventHandler eventHandler = mock(EventHandler.class);
    given(eventHandler.supports(event)).willReturn(true);
    given(eventHandlers.stream()).willReturn(Stream.of(eventHandler));

    hotArticleService.handleEvent(event);

    verify(eventHandler, never()).handle(event);
    verify(hotArticleScoreUpdater).update(event, eventHandler);

  }
}
