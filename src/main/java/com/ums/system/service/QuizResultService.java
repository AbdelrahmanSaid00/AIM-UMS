package com.ums.system.service;

import com.ums.system.model.QuizResult;
import java.util.List;

public interface QuizResultService {
    void saveResult(QuizResult result);
    List<QuizResult> getResultsByStudentId(int studentId);
    List<QuizResult> getResultsByQuizId(int quizId);
}
