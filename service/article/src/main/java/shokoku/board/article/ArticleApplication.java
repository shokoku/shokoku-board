package shokoku.board.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "shokoku.board")
@SpringBootApplication
@EnableJpaRepositories(basePackages = "shokoku.board")
public class ArticleApplication {
  public static void main(String[] args) {
    SpringApplication.run(ArticleApplication.class, args);
  }
}
