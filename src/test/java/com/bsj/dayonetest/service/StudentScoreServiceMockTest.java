package com.bsj.dayonetest.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bsj.dayonetest.annotation.FastTest;
import com.bsj.dayonetest.controller.response.ExamFailStudentResponse;
import com.bsj.dayonetest.controller.response.ExamPassStudentResponse;
import com.bsj.dayonetest.model.*;
import com.bsj.dayonetest.repository.StudentFailRepository;
import com.bsj.dayonetest.repository.StudentPassRepository;
import com.bsj.dayonetest.repository.StudentScoreRepository;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

class StudentScoreServiceMockTest {

  private StudentScoreRepository studentScoreRepository;
  private StudentPassRepository studentPassRepository;
  private StudentFailRepository studentFailRepository;
  private StudentScoreService studentScoreService;

  @BeforeEach
  public void beforeEach() {
    studentScoreRepository = mock(StudentScoreRepository.class);
    studentPassRepository = mock(StudentPassRepository.class);
    studentFailRepository = mock(StudentFailRepository.class);
    studentScoreService =
        new StudentScoreService(
            studentScoreRepository, studentPassRepository, studentFailRepository);
  }

  @FastTest
  @DisplayName("첫번째 Mock 테스트")
  void firstSaveScoreMockTest() {

    String givenStudentName = "bsj";
    String givenExam = "testExam";
    Integer givenKorScore = 80;
    Integer givenEnglishScore = 100;
    Integer givenMathScore = 60;
    studentScoreService.saveScore(
        givenStudentName, givenExam, givenKorScore, givenEnglishScore, givenMathScore);
  }

  @FastTest
  @DisplayName("성적 저장 로직 검증 / 60점 이상인 경우")
  void saveScoreMockTest() {
    // given : 평균점수가 60점 이상인 경우
    String givenStudentName = "bsj";
    String givenExam = "testExam";
    Integer givenKorScore = 80;
    Integer givenEnglishScore = 100;
    Integer givenMathScore = 60;
    // when
    studentScoreService.saveScore(
        givenStudentName, givenExam, givenKorScore, givenEnglishScore, givenMathScore);

    // then
    verify(studentScoreRepository, times(1)).save(any());
    verify(studentPassRepository, times(1)).save(any());
    verify(studentFailRepository, times(0)).save(any());
  }

  @FastTest
  @DisplayName("성적 저장 로직 검증 / 60점 미만인 경우")
  void saveScoreMockTest2() {
    // given : 평균점수가 60점 이상인 경우
    StudentScore expectStudentScore = StudentScoreFixture.failed();

    StudentFail expectStudentFail = StudentFailFixture.create(expectStudentScore);

    ArgumentCaptor<StudentScore> studentScoreArgumentCaptor =
        ArgumentCaptor.forClass(StudentScore.class);
    ArgumentCaptor<StudentFail> studentFailArgumentCaptor =
        ArgumentCaptor.forClass(StudentFail.class);

    // when
    studentScoreService.saveScore(
        expectStudentScore.getStudentName(),
        expectStudentScore.getExam(),
        expectStudentScore.getKorScore(),
        expectStudentScore.getEnglishScore(),
        expectStudentScore.getMathScore());

    // then
    verify(studentScoreRepository, times(1)).save(studentScoreArgumentCaptor.capture());

    StudentScore capturedStudentScore = studentScoreArgumentCaptor.getValue();
    assertThat(capturedStudentScore.getExam()).isEqualTo(expectStudentScore.getExam());
    assertThat(capturedStudentScore.getStudentName())
        .isEqualTo(expectStudentScore.getStudentName());
    assertThat(capturedStudentScore.getKorScore()).isEqualTo(expectStudentScore.getKorScore());
    assertThat(capturedStudentScore.getEnglishScore())
        .isEqualTo(expectStudentScore.getEnglishScore());
    assertThat(capturedStudentScore.getMathScore()).isEqualTo(expectStudentScore.getMathScore());

    verify(studentPassRepository, times(0)).save(any());

    verify(studentFailRepository, times(1)).save(studentFailArgumentCaptor.capture());
    StudentFail capturedStudentFail = studentFailArgumentCaptor.getValue();
    assertThat(capturedStudentFail.getStudentName()).isEqualTo(expectStudentFail.getStudentName());
    assertThat(capturedStudentFail.getAvgScore()).isEqualTo(expectStudentFail.getAvgScore());
    assertThat(capturedStudentFail.getExam()).isEqualTo(expectStudentFail.getExam());
  }

  @FastTest
  @DisplayName("합격자 명단 가죠오기 검증")
  void getPassStudentListTest() {
    // given
    String givenExam = "testExam";
    StudentPass expectedStudent1 = StudentPassFixture.create("bsj1", givenExam);
    StudentPass expectedStudent2 = StudentPassFixture.create("bsj2", givenExam);
    StudentPass notExpectedStudent = StudentPassFixture.create("bsj3", "secondExam");

    when(studentPassRepository.findAll())
        .thenReturn(List.of(expectedStudent1, expectedStudent2, notExpectedStudent));

    // when
    List<ExamPassStudentResponse> responses = studentScoreService.getPassStudentList("testExam");
    var expectedResponses =
        Stream.of(expectedStudent1, expectedStudent2)
            .map(pass -> new ExamPassStudentResponse(pass.getStudentName(), pass.getAvgScore()))
            .toList();

    assertThatIterable(responses).isEqualTo(expectedResponses);
  }

  @FastTest
  @DisplayName("불합격자 명단 가죠오기 검증")
  void getFailStudentList() {
    // given
    String givenExam = "testExam";
    StudentFail expectedStudent1 = StudentFailFixture.create("bsj1", givenExam);
    StudentFail expectedStudent2 = StudentFailFixture.create("bsj2", givenExam);
    StudentFail notExpectedStudent = StudentFailFixture.create("bsj3", "secondExam");

    when(studentFailRepository.findAll())
        .thenReturn(List.of(expectedStudent1, expectedStudent2, notExpectedStudent));

    // when
    List<ExamFailStudentResponse> responses = studentScoreService.getFailStudentList("testExam");
    var expectedResponses =
        Stream.of(expectedStudent1, expectedStudent2)
            .map(pass -> new ExamFailStudentResponse(pass.getStudentName(), pass.getAvgScore()))
            .toList();

    // then
    assertThatIterable(responses).isEqualTo(expectedResponses);
  }
}
