package com.bsj.dayonetest;

import static org.assertj.core.api.Assertions.assertThat;

import com.bsj.dayonetest.annotation.SlowTest;
import com.bsj.dayonetest.model.StudentScore;
import com.bsj.dayonetest.model.StudentScoreFixture;
import com.bsj.dayonetest.repository.StudentScoreRepository;
import com.bsj.dayonetest.testbase.IntegrationTest;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DayonetestApplicationTests extends IntegrationTest {

  @Autowired private StudentScoreRepository studentScoreRepository;

  @Autowired private EntityManager entityManager;

  @SlowTest
  void contextLoads() {
    var studentScore = StudentScoreFixture.passed();
    StudentScore savedStudentScore = studentScoreRepository.save(studentScore);

    entityManager.flush();
    entityManager.clear();

    StudentScore queryStudentScore =
        studentScoreRepository.findById(savedStudentScore.getId()).orElseThrow();
    assertThat(savedStudentScore.getId()).isEqualTo(queryStudentScore.getId());
  }
}
