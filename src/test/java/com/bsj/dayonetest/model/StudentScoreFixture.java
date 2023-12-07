package com.bsj.dayonetest.model;

public class StudentScoreFixture {

  public static StudentScore passed() {
    return StudentScore.builder()
        .exam("defaultExam")
        .studentName("defaultName")
        .korScore(90)
        .englishScore(80)
        .mathScore(100)
        .build();
  }

  public static StudentScore failed() {
    return StudentScore.builder()
        .exam("defaultExam")
        .studentName("defaultName")
        .korScore(50)
        .englishScore(40)
        .mathScore(40)
        .build();
  }
}
