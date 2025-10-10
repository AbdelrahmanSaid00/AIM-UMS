package com.ums.system.service;

import com.ums.system.dao.QuizDAO;
import com.ums.system.model.Quiz;
import com.ums.system.service.QuizService;
import java.util.List;

public class QuizServiceImpl implements QuizService {

    private final QuizDAO quizDAO;

    public QuizServiceImpl(QuizDAO quizDAO) {
        this.quizDAO = quizDAO;
    }

    @Override
    public void createQuiz(Quiz quiz) {
        quizDAO.insert(quiz);
    }

    @Override
    public void updateQuiz(Quiz quiz) {
        quizDAO.update(quiz);
    }

    @Override
    public void deleteQuiz(int id) {
        quizDAO.delete(id);
    }

    @Override
    public Quiz getQuizById(int id) {
        return quizDAO.getById(id);
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        return quizDAO.getAll();
    }

    @Override
    public List<Quiz> getQuizzesByCourseCode(String courseCode) {
        return quizDAO.getByCourseCode(courseCode);
    }
}
