package shokoku.board.article.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PageLimitCalculatorTest {

  @Test
  void calculatePageLimitTest() {
    calculateMovablePageCountTest(1L, 30L, 10L, 301L);
    calculateMovablePageCountTest(7L, 30L, 10L, 301L);
    calculateMovablePageCountTest(10L, 30L, 10L, 301L);
    calculateMovablePageCountTest(11L, 30L, 10L, 601L);
    calculateMovablePageCountTest(12L, 30L, 10L, 601L);

  }

  void calculateMovablePageCountTest(Long page, Long pageSize, Long movablePageCount, Long expected) {
    Long result = PageLimitCalculator.calculatePageLimit(page, pageSize, movablePageCount);
    assertThat(result).isEqualTo(expected);
  }
}
