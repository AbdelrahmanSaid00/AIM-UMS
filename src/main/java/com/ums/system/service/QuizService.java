package com.ums.system.service;

import com.ums.system.model.Quiz;
import java.util.List;

public interface QuizService {
    void createQuiz(Quiz quiz);
    void updateQuiz(Quiz quiz);
    void deleteQuiz(int id);
    Quiz getQuizById(int id);
    List<Quiz> getAllQuizzes();
    List<Quiz> getQuizzesByCourseCode(String courseCode);
}
