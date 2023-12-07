package com.bsj.dayonetest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LombokTesTDataTest {

  @Test
  void testDataTest() {
    TestData testData = new TestData();
    testData.setName("bsj");

    assertThat(testData.getName()).isEqualTo("bsj");
  }
}
