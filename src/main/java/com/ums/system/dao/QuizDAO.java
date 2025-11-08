package com.ums.system.dao;

import com.ums.system.model.Quiz;
import java.util.List;

public interface QuizDAO {
    void insert(Quiz quiz);
    void update(Quiz quiz);
    void delete(int id);
    Quiz getById(int id);
    List<Quiz> getAll();
    List<Quiz> getByCourseCode(String courseCode);
    List<Quiz> getByInstructorId(int instructorId);
}
