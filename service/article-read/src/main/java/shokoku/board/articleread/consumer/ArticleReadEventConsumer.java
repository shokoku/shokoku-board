package shokoku.board.articleread.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import shokoku.board.articleread.service.ArticleReadService;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventPayload;
import shokoku.board.common.event.EventType;

import static shokoku.board.common.event.EventType.Topic.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleReadEventConsumer {
  private final ArticleReadService articleReadService;

  @KafkaListener(topics = {
          SHOKOKU_BOARD_ARTICLE,
          SHOKOKU_BOARD_COMMENT,
          SHOKOKU_BOARD_LIKE
  })
  public void listen(String message, Acknowledgment ack) {
    log.info("[ArticleReadEventConsumer.listen] received message = {}", message);
    Event<EventPayload> event = Event.fromJson(message);
    if (event != null) {
      articleReadService.handleEvent(event);
    }
    ack.acknowledge();
  }
}
