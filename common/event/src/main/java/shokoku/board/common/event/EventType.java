package shokoku.board.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shokoku.board.common.event.payload.*;

import static shokoku.board.common.event.EventType.Topic.*;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
  ARTICLE_CREATED(ArticleCreatedEventPayload.class, SHOKOKU_BOARD_ARTICLE),
  ARTICLE_UPDATED(ArticleUpdatedEventPayload.class, SHOKOKU_BOARD_ARTICLE),
  ARTICLE_DELETED(ArticleDeletedEventPayload.class, SHOKOKU_BOARD_ARTICLE),
  COMMENT_CREATED(CommentCreatedEventPayload.class, SHOKOKU_BOARD_COMMENT),
  COMMENT_DELETED(CommentDeletedEventPayload.class, SHOKOKU_BOARD_COMMENT),
  ARTICLE_LIKED(ArticleLikedEventPayload.class, SHOKOKU_BOARD_LIKE),
  ARTICLE_UNLIKED(ArticleUnlikedEventPayload.class, SHOKOKU_BOARD_LIKE),
  ARTICLE_VIEWED(ArticleViewedEventPayload.class, SHOKOKU_BOARD_VIEW),;

  private final Class<? extends EventPayload> payloadClass;
  private final String topic;

  public static EventType from(String type) {
    try {
      return valueOf(type);
    } catch (Exception e) {
      log.error("[EventType.from] type = [{}]", type, e);
      return null;
    }
  }

  public static class Topic {
    public static final String SHOKOKU_BOARD_ARTICLE = "shokoku-board-article";
    public static final String SHOKOKU_BOARD_COMMENT = "shokoku-board-comment";
    public static final String SHOKOKU_BOARD_LIKE = "shokoku-board-like";
    public static final String SHOKOKU_BOARD_VIEW = "shokoku-board-view";
  }
}
