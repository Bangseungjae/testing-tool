package com.bsj.dayonetest.controller.response;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExamFailStudentResponse {
  private final String studentName;
  private final Double avgScore;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ExamFailStudentResponse that)) return false;
    return Objects.equals(studentName, that.studentName) && Objects.equals(avgScore, that.avgScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentName, avgScore);
  }
}
