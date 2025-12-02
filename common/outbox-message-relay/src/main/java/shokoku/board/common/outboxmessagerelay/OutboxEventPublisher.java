package shokoku.board.common.outboxmessagerelay;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import shokoku.board.common.event.Event;
import shokoku.board.common.event.EventPayload;
import shokoku.board.common.event.EventType;
import shokoku.board.common.snowflake.Snowflake;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {
  private final Snowflake outboxIdSnowflake = new Snowflake();
  private final Snowflake eventIdSnowflake = new Snowflake();
  private final ApplicationEventPublisher applicationEventPublisher;

  public void publish(EventType type, EventPayload payload, Long shardKey) {
    Outbox outbox = Outbox.create(
            outboxIdSnowflake.nextId(),
            type,
            Event.of(
                    eventIdSnowflake.nextId(), type, payload
            ).toJson(),
            shardKey & MessageRelayConstants.SHARD_COUNT
    );
    applicationEventPublisher.publishEvent(OutboxEvent.of(outbox));
  }
}
