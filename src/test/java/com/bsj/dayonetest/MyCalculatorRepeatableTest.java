package com.bsj.dayonetest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MyCalculatorRepeatableTest {

  @DisplayName("덧셈을 5번 반복하여 테스트")
  @RepeatedTest(5)
  void repeatedAddTest() {
    MyCalculator myCalculator = new MyCalculator();

    myCalculator.add(10.0);

    assertThat(myCalculator.getResult()).isEqualTo(10.0);
  }

  @DisplayName("덧셈을 5번 반복하여 테스트")
  @ParameterizedTest
  @Tag("fast")
  @MethodSource("parameterizedTestsParameters")
  void parameterizedTest(Double addValue, Double expectedValue) {
    MyCalculator myCalculator = new MyCalculator(0.0);

    myCalculator.add(addValue);

    assertThat(myCalculator.getResult()).isEqualTo(expectedValue);
  }

  public static Stream<Arguments> parameterizedTestsParameters() {
    return Stream.of(
        Arguments.of(10.0, 10.0),
        Arguments.of(8.0, 8.0),
        Arguments.of(4.0, 4.0),
        Arguments.of(16.0, 16.0),
        Arguments.of(13.0, 13.0));
  }

  @DisplayName("파라미터를 넣으며 사친연삭 2번 반복 테스트")
  @ParameterizedTest
  @MethodSource("parameterizedComplicatedCalculateTestParameters")
  void parameterizedComplicatedTest(
      Double addValue,
      Double minusValue,
      Double multiplyValue,
      Double divideValue,
      Double expectedValue) {
    // given
    MyCalculator myCalculator = new MyCalculator(0.0);

    // when
    Double result =
        myCalculator
            .add(addValue)
            .minus(minusValue)
            .multiply(multiplyValue)
            .divide(divideValue)
            .getResult();

    assertThat(result).isEqualTo(expectedValue);
  }

  private static Stream<Arguments> parameterizedComplicatedCalculateTestParameters() {
    return Stream.of(Arguments.of(10.0, 4.0, 2.0, 3.0, 4.0), Arguments.of(4.0, 2.0, 4.0, 4.0, 2.0));
  }
}
