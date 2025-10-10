package com.ums.system.service;

import com.ums.system.model.Question;
import java.util.List;

public interface QuestionService {
    void addQuestion(Question question, int quizId);
    void updateQuestion(Question question);
    void deleteQuestion(int id);
    List<Question> getQuestionsByQuizId(int quizId);
}
