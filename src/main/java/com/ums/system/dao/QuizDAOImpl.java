package com.ums.system.dao;

import com.ums.system.model.Question;
import com.ums.system.model.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAOImpl implements QuizDAO {

    private final Connection connection;
    private final QuestionDAO questionDAO;

    public QuizDAOImpl(Connection connection, QuestionDAO questionDAO) {
        this.connection = connection;
        this.questionDAO = questionDAO;
    }

    private boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM quizzes WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking quiz existence: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void insert(Quiz quiz) {
        if (quiz == null || quiz.getTitle() == null || quiz.getCourseCode() == null) {
            System.err.println("Invalid quiz data — cannot insert null values.");
            return;
        }

        String quizSql = "INSERT INTO quizzes (title, course_code) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(quizSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, quiz.getTitle());
            ps.setString(2, quiz.getCourseCode());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int quizId = rs.getInt(1);
                    if (quiz.getQuestions() != null && !quiz.getQuestions().isEmpty()) {
                        for (Question q : quiz.getQuestions()) {
                            questionDAO.insert(q, quizId);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting quiz: " + e.getMessage());
        }
    }

    @Override
    public void update(Quiz quiz) {
        if (quiz == null || quiz.getId() <= 0) {
            System.err.println("Invalid quiz ID — cannot update.");
            return;
        }
        if (!existsById(quiz.getId())) {
            System.err.println("Quiz with ID " + quiz.getId() + " does not exist.");
            return;
        }

        String sql = "UPDATE quizzes SET title = ?, course_code = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, quiz.getTitle());
            ps.setString(2, quiz.getCourseCode());
            ps.setInt(3, quiz.getId());
            ps.executeUpdate();

            if (quiz.getQuestions() != null && !quiz.getQuestions().isEmpty()) {
                for (Question q : quiz.getQuestions()) {
                    questionDAO.update(q);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating quiz: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        if (!existsById(id)) {
            System.err.println("Quiz with ID " + id + " does not exist — cannot delete.");
            return;
        }

        try {
            List<Question> questions = questionDAO.getByQuizId(id);
            for (Question q : questions) {
                questionDAO.delete(q.getId());
            }

            String sql = "DELETE FROM quizzes WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Error deleting quiz: " + e.getMessage());
        }
    }

    @Override
    public Quiz getById(int id) {
        String sql = "SELECT * FROM quizzes WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    List<Question> questions = questionDAO.getByQuizId(id);
                    return new Quiz(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("course_code"),
                            questions
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching quiz by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Quiz> getAll() {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM quizzes";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int quizId = rs.getInt("id");
                List<Question> questions = questionDAO.getByQuizId(quizId);

                quizzes.add(new Quiz(
                        quizId,
                        rs.getString("title"),
                        rs.getString("course_code"),
                        questions
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all quizzes: " + e.getMessage());
        }
        return quizzes;
    }

    @Override
    public List<Quiz> getByCourseCode(String courseCode) {
        List<Quiz> quizzes = new ArrayList<>();
        if (courseCode == null || courseCode.isBlank()) {
            System.err.println("Invalid course code — cannot fetch quizzes.");
            return quizzes;
        }

        String sql = "SELECT id, title, course_code FROM quizzes WHERE course_code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int quizId = rs.getInt("id");
                    List<Question> questions = questionDAO.getByQuizId(quizId);

                    quizzes.add(new Quiz(
                            quizId,
                            rs.getString("title"),
                            rs.getString("course_code"),
                            questions
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching quizzes by course code: " + e.getMessage());
        }
        return quizzes;
    }

    @Override
    public List<Quiz> getByInstructorId(int instructorId) {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT q.id, q.title, q.course_code " +
                     "FROM quizzes q " +
                     "JOIN courses c ON q.course_code = c.code " +
                     "WHERE c.instructor_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, instructorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int quizId = rs.getInt("id");
                    List<Question> questions = questionDAO.getByQuizId(quizId);

                    quizzes.add(new Quiz(
                            quizId,
                            rs.getString("title"),
                            rs.getString("course_code"),
                            questions
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching quizzes by instructor ID: " + e.getMessage());
        }
        return quizzes;
    }
}
