package shokoku.board.hotarticle.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventPayload;
import shokoku.board.common.event.EventType;
import shokoku.board.hotarticle.service.HotArticleService;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleEventConsumer {
  private final HotArticleService hotArticleService;

  @KafkaListener(topics = {
          EventType.Topic.SHOKOKU_BOARD_ARTICLE,
          EventType.Topic.SHOKOKU_BOARD_COMMENT,
          EventType.Topic.SHOKOKU_BOARD_LIKE,
          EventType.Topic.SHOKOKU_BOARD_VIEW
  })
  public void listen(String message, Acknowledgment ack) {
    log.info("[HotArticleEventConsumer.listen] received message = {}", message);
    Event<EventPayload> event = Event.fromJson(message);
    if (event != null) {
      hotArticleService.handleEvent(event);
    }
    ack.acknowledge();

  }
}
