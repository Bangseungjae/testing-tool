package com.bsj.dayonetest.service;

import com.bsj.dayonetest.MyCalculator;
import com.bsj.dayonetest.controller.response.ExamFailStudentResponse;
import com.bsj.dayonetest.controller.response.ExamPassStudentResponse;
import com.bsj.dayonetest.model.StudentFail;
import com.bsj.dayonetest.model.StudentPass;
import com.bsj.dayonetest.model.StudentScore;
import com.bsj.dayonetest.repository.StudentFailRepository;
import com.bsj.dayonetest.repository.StudentPassRepository;
import com.bsj.dayonetest.repository.StudentScoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentScoreService {
  private final StudentScoreRepository studentScoreRepository;
  private final StudentPassRepository studentPassRepository;
  private final StudentFailRepository studentFailRepository;

  public void saveScore(
      String studentName, String exam, Integer korScore, Integer englishScore, Integer mathScore) {
    StudentScore studentScore =
        StudentScore.builder()
            .exam(exam)
            .studentName(studentName)
            .korScore(korScore)
            .englishScore(englishScore)
            .mathScore(mathScore)
            .build();
    studentScoreRepository.save(studentScore);
    MyCalculator calculator = new MyCalculator();
    Double avgScore =
        calculator
            .add(korScore.doubleValue())
            .add(englishScore.doubleValue())
            .add(mathScore.doubleValue())
            .divide(3.0)
            .getResult();

    if (avgScore >= 60) {
      StudentPass studentPass =
          StudentPass.builder().exam(exam).studentName(studentName).avgScore(avgScore).build();
      studentPassRepository.save(studentPass);
    } else {
      StudentFail studentFail =
          StudentFail.builder().exam(exam).studentName(studentName).avgScore(avgScore).build();
      studentFailRepository.save(studentFail);
    }
  }

  public List<ExamPassStudentResponse> getPassStudentList(String exam) {
    List<StudentPass> studentPasses = studentPassRepository.findAll();
    return studentPasses.stream()
        .filter(pass -> pass.getExam().equals(exam))
        .map(pass -> new ExamPassStudentResponse(pass.getStudentName(), pass.getAvgScore()))
        .toList();
  }

  public List<ExamFailStudentResponse> getFailStudentList(String exam) {
    List<StudentFail> studentPasses = studentFailRepository.findAll();
    return studentPasses.stream()
        .filter(fail -> fail.getExam().equals(exam))
        .map(fail -> new ExamFailStudentResponse(fail.getStudentName(), fail.getAvgScore()))
        .toList();
  }
}
