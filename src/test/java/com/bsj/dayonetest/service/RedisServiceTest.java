package com.bsj.dayonetest.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.bsj.dayonetest.annotation.SlowTest;
import com.bsj.dayonetest.testbase.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

class RedisServiceTest extends IntegrationTest {

  @Autowired private RedisService redisService;

  @SlowTest
  void redisGetSetTest() {
    // given
    String expectedValue = "hello";
    String key = "hi";

    // when
    redisService.set(key, expectedValue);

    // then
    String actualValue = redisService.get(key);
    assertThat(actualValue).isEqualTo(expectedValue);
  }
}
