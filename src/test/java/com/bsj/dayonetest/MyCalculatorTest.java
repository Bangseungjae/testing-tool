package com.bsj.dayonetest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MyCalculatorTest {

  @Test
  @DisplayName("MyCalculator 덧셈 테스트")
  void addTest() {
    MyCalculator myCalculator = new MyCalculator();

    myCalculator.add(10.0);

    assertThat(myCalculator.getResult()).isEqualTo(10.0);
  }

  @Test
  @DisplayName("MyCalculator 뺄셈 테스트")
  void minusTest() {
    // Arrange - 준비
    MyCalculator myCalculator = new MyCalculator(10.0);
    // Act - 행동
    myCalculator.minus(5.0);
    // Assert - 단언/검증
    assertThat(myCalculator.getResult()).isEqualTo(5.0);
  }

  @Test
  @DisplayName("MyCalculator 곱셈 테스트")
  void multiplyTest() {
    MyCalculator myCalculator = new MyCalculator(2.0);
    myCalculator.multiply(2.0);

    assertThat(myCalculator.getResult()).isEqualTo(4.0);
  }

  @Test
  @DisplayName("MyCalculator 나눗셈 테스트")
  void divideTest() {
    MyCalculator myCalculator = new MyCalculator(10.0);
    myCalculator.divide(2.0);

    assertThat(myCalculator.getResult()).isEqualTo(5.0);
  }

  @Test
  @DisplayName("MyCalculator 사칙연산 테스트")
  void complicatedCalculateTest() {
    // given
    MyCalculator myCalculator = new MyCalculator(0.0);

    // when
    Double result = myCalculator.add(10.0).minus(4.0).multiply(2.0).divide(3.0).getResult();

    assertThat(result).isEqualTo(4.0);
  }

  @Test
  @DisplayName("MyCalculator 0으로 나누었을 때는 ZeroDivisionException을 발생시켜야 함")
  void divideZeroTest() {
    // given
    MyCalculator myCalculator = new MyCalculator(10.0);

    // when & then
    assertThatThrownBy(() -> myCalculator.divide(0.0))
        .isInstanceOf(MyCalculator.ZeroDivisionException.class);
  }
}
