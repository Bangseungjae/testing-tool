package com.bsj.dayonetest.controller;

import com.bsj.dayonetest.controller.request.SaveExamScoreRequest;
import com.bsj.dayonetest.controller.response.ExamFailStudentResponse;
import com.bsj.dayonetest.controller.response.ExamPassStudentResponse;
import com.bsj.dayonetest.service.StudentScoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ScoreApi {

  private final StudentScoreService studentScoreService;

  @PutMapping("/exam/{exam}/score")
  public void save(@RequestBody SaveExamScoreRequest request, @PathVariable("exam") String exam) {
    studentScoreService.saveScore(
        request.getStudentName(),
        exam,
        request.getKorScore(),
        request.getEnglishScore(),
        request.getMathScore());
  }

  @GetMapping("/exam/{exam}/pass")
  public List<ExamPassStudentResponse> pass(@PathVariable("exam") String exam) {
    return studentScoreService.getPassStudentList(exam);
  }

  @GetMapping("/exam/{exam}/fail")
  public List<ExamFailStudentResponse> fail(@PathVariable("exam") String exam) {
    return studentScoreService.getFailStudentList(exam);
  }
}
