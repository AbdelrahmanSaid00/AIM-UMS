package com.ums.system.service;

import com.ums.system.dao.QuestionDAO;
import com.ums.system.model.Question;
import com.ums.system.service.QuestionService;
import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    private final QuestionDAO questionDAO;

    public QuestionServiceImpl(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    @Override
    public void addQuestion(Question question, int quizId) {
        questionDAO.insert(question, quizId);
    }

    @Override
    public void updateQuestion(Question question) {
        questionDAO.update(question);
    }

    @Override
    public void deleteQuestion(int id) {
        questionDAO.delete(id);
    }

    @Override
    public List<Question> getQuestionsByQuizId(int quizId) {
        return questionDAO.getByQuizId(quizId);
    }
}
