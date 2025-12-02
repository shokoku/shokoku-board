package shokoku.board.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shokoku.board.article.entity.Article;
import shokoku.board.article.entity.BoardArticleCount;
import shokoku.board.article.repository.ArticleRepository;
import shokoku.board.article.repository.BoardArticleCountRepository;
import shokoku.board.article.service.request.ArticleCreateRequest;
import shokoku.board.article.service.request.ArticleUpdateRequest;
import shokoku.board.article.service.response.ArticlePageResponse;
import shokoku.board.article.service.response.ArticleResponse;
import shokoku.board.common.event.EventType;
import shokoku.board.common.event.payload.ArticleCreatedEventPayload;
import shokoku.board.common.event.payload.ArticleDeletedEventPayload;
import shokoku.board.common.event.payload.ArticleUpdatedEventPayload;
import shokoku.board.common.outboxmessagerelay.OutboxEventPublisher;
import shokoku.board.common.snowflake.Snowflake;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final Snowflake snowflake = new Snowflake();
  private final OutboxEventPublisher outboxEventPublisher;
  private final ArticleRepository articleRepository;
  private final BoardArticleCountRepository boardArticleCountRepository;

  @Transactional
  public ArticleResponse create(ArticleCreateRequest request) {
    Article article = articleRepository.save(
            Article.create(snowflake.nextId(), request.getTitle(), request.getContent(), request.getBoardId(), request.getWriterId())
    );
    int result = boardArticleCountRepository.increase(article.getBoardId());
    if (result == 0) {
      boardArticleCountRepository.save(
              BoardArticleCount.init(request.getBoardId(), 1L)
      );
    }
    outboxEventPublisher.publish(
            EventType.ARTICLE_CREATED,
            ArticleCreatedEventPayload.builder()
                    .articleId(article.getArticleId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .boarId(article.getBoardId())
                    .writerId(article.getWriterId())
                    .createdAt(article.getCreatedAt())
                    .modifiedAt(article.getModifiedAt())
                    .boardArticleCount(count(article.getBoardId()))
                    .build(),
            article.getBoardId()
    );

    return ArticleResponse.from(article);
  }

  @Transactional
  public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {
    Article article = articleRepository.findById(articleId).orElseThrow();
    article.update(request.getTitle(), request.getContent());
    outboxEventPublisher.publish(
            EventType.ARTICLE_UPDATED,
            ArticleUpdatedEventPayload.builder()
                    .articleId(article.getArticleId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .boarId(article.getBoardId())
                    .writerId(article.getWriterId())
                    .createdAt(article.getCreatedAt())
                    .modifiedAt(article.getModifiedAt())
                    .build(),
            article.getBoardId()
    );
    return ArticleResponse.from(article);
  }

  @Transactional
  public ArticleResponse read(Long articleId) {
    return ArticleResponse.from(articleRepository.findById(articleId).orElseThrow());
  }

  @Transactional
  public void delete(Long articleId) {
    Article article = articleRepository.findById(articleId).orElseThrow();
    articleRepository.delete(article);
    boardArticleCountRepository.decrease(article.getBoardId());
    outboxEventPublisher.publish(
            EventType.ARTICLE_DELETED,
            ArticleDeletedEventPayload.builder()
                    .articleId(article.getArticleId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .boarId(article.getBoardId())
                    .writerId(article.getWriterId())
                    .createdAt(article.getCreatedAt())
                    .modifiedAt(article.getModifiedAt())
                    .boardArticleCount(count(article.getBoardId()))
                    .build(),
            article.getBoardId());
  }

  @Transactional
  public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize) {
    return ArticlePageResponse.of(
            articleRepository.findAll(boardId, (page - 1) * pageSize, pageSize).stream()
                    .map(ArticleResponse::from)
                    .toList(),
            articleRepository.count(
                    boardId,
                    PageLimitCalculator.calculatePageLimit(page, pageSize, 10L)
            )
    );
  }

  @Transactional
  public List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long pageSize, Long lastArticleId) {
    List<Article> articles = lastArticleId == null ?
            articleRepository.findAllInfiniteScroll(boardId, pageSize) :
            articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId);
    return articles.stream().map(ArticleResponse::from).toList();
  }

  public Long count(Long boardId) {
    return boardArticleCountRepository.findById(boardId)
            .map(BoardArticleCount::getArticleCount).orElse(0L);
  }

}
