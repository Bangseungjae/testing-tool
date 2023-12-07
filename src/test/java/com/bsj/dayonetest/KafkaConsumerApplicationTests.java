package com.bsj.dayonetest;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

import com.bsj.dayonetest.annotation.SlowTest;
import com.bsj.dayonetest.service.KafkaConsumerService;
import com.bsj.dayonetest.service.KafkaProducerService;
import com.bsj.dayonetest.testbase.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

@Order(0)
@DirtiesContext(classMode = AFTER_CLASS) // 애플리케이션 컨텍스트 공유를 하지 않게한다. 이를 통해 테스트간 격리한다.
class KafkaConsumerApplicationTests extends IntegrationTest {

  @Autowired private KafkaProducerService kafkaProducerService;

  // spring bean 상태의 객체를 목으로 가져올 수 있다. (가짜 객체)
  @MockBean private KafkaConsumerService kafkaConsumerService;

  @SlowTest
  public void kafkaSendAndConsumeTest() {
    String topic = "test-topic";
    String expectValue = "expect-value";

    kafkaProducerService.send(topic, expectValue);

    var stringCaptor = ArgumentCaptor.forClass(String.class);
    System.out.println("-----------mock-----------start");
    Mockito.verify(kafkaConsumerService, Mockito.timeout(5000).times(1))
        .process(stringCaptor.capture());
    System.out.println("-----------mock-----------finish");

    Assertions.assertEquals(expectValue, stringCaptor.getValue());
  }
}
