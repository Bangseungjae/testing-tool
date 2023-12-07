package com.bsj.dayonetest.model;

import com.bsj.dayonetest.MyCalculator;

public class StudentPassFixture {

  public static StudentPass create(StudentScore studentScore) {
    var calculator = new MyCalculator();
    return StudentPass.builder()
        .exam(studentScore.getExam())
        .studentName(studentScore.getStudentName())
        .avgScore(
            calculator
                .add(studentScore.getKorScore().doubleValue())
                .add(studentScore.getEnglishScore().doubleValue())
                .add(studentScore.getMathScore().doubleValue())
                .divide(3.0)
                .getResult())
        .build();
  }

  public static StudentPass create(String studentName, String exam) {
    return StudentPass.builder().studentName(studentName).exam(exam).avgScore(70.0).build();
  }
}
