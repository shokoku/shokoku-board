package shokoku.board.article.service.request;

import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@ToString
public class ArticleCreateRequest {
  private String title;
  private String content;
  private Long writerId;
  private Long boardId;
}
