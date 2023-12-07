package com.bsj.dayonetest.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.bsj.dayonetest.MyCalculator;
import com.bsj.dayonetest.annotation.SlowTest;
import com.bsj.dayonetest.controller.response.ExamFailStudentResponse;
import com.bsj.dayonetest.controller.response.ExamPassStudentResponse;
import com.bsj.dayonetest.model.StudentScore;
import com.bsj.dayonetest.model.StudentScoreFixture;
import com.bsj.dayonetest.testbase.IntegrationTest;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

class StudentScoreServiceIntegrationTest extends IntegrationTest {

  @Autowired private StudentScoreService studentScoreService;

  @Autowired private EntityManager entityManager;

  @SlowTest
  void savePassedStudentScoreTest() {
    // given
    var studentScore = StudentScoreFixture.passed();

    // when
    studentScoreService.saveScore(
        studentScore.getStudentName(),
        studentScore.getExam(),
        studentScore.getKorScore(),
        studentScore.getEnglishScore(),
        studentScore.getMathScore());
    entityManager.flush();
    entityManager.clear();

    // then
    List<ExamPassStudentResponse> passedStudentResponses =
        studentScoreService.getPassStudentList(studentScore.getExam());

    assertThat(passedStudentResponses).hasSize(1);

    ExamPassStudentResponse passedStudentResponse = passedStudentResponses.get(0);

    assertThat(passedStudentResponse.getStudentName()).isEqualTo(studentScore.getStudentName());
    MyCalculator calculator = new MyCalculator(0.0);
    assertThat(passedStudentResponse.getAvgScore())
        .isEqualTo(
            calculator
                .add(studentScore.getKorScore().doubleValue())
                .add(studentScore.getEnglishScore().doubleValue())
                .add(studentScore.getMathScore().doubleValue())
                .divide(3.0)
                .getResult());
  }

  @SlowTest
  void saveFailedStudentScoreTest() {
    // given
    StudentScore studentScore = StudentScoreFixture.failed();

    // when
    studentScoreService.saveScore(
        studentScore.getStudentName(),
        studentScore.getExam(),
        studentScore.getKorScore(),
        studentScore.getEnglishScore(),
        studentScore.getMathScore());
    entityManager.flush();
    entityManager.clear();

    // then
    var failedStudentResponses = studentScoreService.getFailStudentList(studentScore.getExam());
    assertThat(failedStudentResponses.size()).isEqualTo(1);
    ExamFailStudentResponse failedStudentResponse = failedStudentResponses.get(0);
    assertThat(failedStudentResponse.getStudentName()).isEqualTo(studentScore.getStudentName());

    MyCalculator calculator = new MyCalculator(0.0);
    assertThat(failedStudentResponse.getAvgScore())
        .isEqualTo(
            calculator
                .add(studentScore.getKorScore().doubleValue())
                .add(studentScore.getEnglishScore().doubleValue())
                .add(studentScore.getMathScore().doubleValue())
                .divide(3.0)
                .getResult());
  }
}
