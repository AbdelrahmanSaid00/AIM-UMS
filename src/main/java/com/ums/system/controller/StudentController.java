package com.ums.system.controller;

import com.ums.system.App;
import com.ums.system.model.*;
import com.ums.system.service.*;
import com.ums.system.util.ServiceLocator;
import com.ums.system.dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

/**
 * StudentController - Handles Student panel functionality
 * Provides access to student features from CLI
 */
public class StudentController {

    @FXML private Label welcomeLabel;
    @FXML private Label userInfoLabel;

    // Available Courses Tab
    @FXML private TableView<Course> availableCoursesTable;
    @FXML private TableColumn<Course, Integer> availCourseIdColumn;
    @FXML private TableColumn<Course, String> availCourseNameColumn;
    @FXML private TableColumn<Course, String> availCourseCodeColumn;
    @FXML private TableColumn<Course, Integer> availCreditsColumn;

    // My Courses Tab
    @FXML private TableView<Course> myCoursesTable;
    @FXML private TableColumn<Course, Integer> myCourseIdColumn;
    @FXML private TableColumn<Course, String> myCourseNameColumn;
    @FXML private TableColumn<Course, String> myCourseCodeColumn;
    @FXML private TableColumn<Course, Integer> myCreditsColumn;

    // Quizzes Tab
    @FXML private ComboBox<Course> quizCourseCombo;
    @FXML private TableView<Quiz> quizzesTable;
    @FXML private TableColumn<Quiz, Integer> quizIdColumn;
    @FXML private TableColumn<Quiz, String> quizTitleColumn;
    @FXML private TableColumn<Quiz, Integer> quizCourseColumn;
    @FXML private TableColumn<Quiz, Integer> quizQuestionsColumn;

    // My Grades Tab
    @FXML private TableView<QuizResult> gradesTable;
    @FXML private TableColumn<QuizResult, Integer> gradeIdColumn;
    @FXML private TableColumn<QuizResult, Integer> gradeQuizColumn;
    @FXML private TableColumn<QuizResult, Double> gradeScoreColumn;
    @FXML private TableColumn<QuizResult, String> gradeDateColumn;
    @FXML private Label averageScoreLabel;

    // Services
    private Student currentStudent;
    private CourseService courseService;
    private StudentService studentService;
    private QuizService quizService;
    private QuizResultService quizResultService;
    private EnrollmentDAO enrollmentDAO;

    /**
     * Initialize method
     */
    @FXML
    public void initialize() {
        // Get services from ServiceLocator
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        courseService = serviceLocator.getCourseService();
        studentService = serviceLocator.getStudentService();
        quizService = serviceLocator.getQuizService();
        quizResultService = serviceLocator.getQuizResultService();
        enrollmentDAO = serviceLocator.getEnrollmentDAO();

        // Set up tables
        setupAvailableCoursesTable();
        setupMyCoursesTable();
        setupQuizzesTable();
        setupGradesTable();
    }

    /**
     * Set current student user
     */
    public void setUser(Student student) {
        this.currentStudent = student;
        welcomeLabel.setText("Welcome, " + student.getName() + "!");
        userInfoLabel.setText("Role: Student | ID: " + student.getId() +
                            " | Level: " + student.getLevel());

        // Load initial data
        loadAvailableCourses();
        loadMyCourses();
        loadMyGrades();
    }

    /**
     * Setup tables
     */
    private void setupAvailableCoursesTable() {
        availCourseIdColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        availCourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        availCourseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        availCreditsColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
    }

    private void setupMyCoursesTable() {
        myCourseIdColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        myCourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        myCourseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        myCreditsColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
    }

    private void setupQuizzesTable() {
        quizIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        quizTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        quizCourseColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        quizQuestionsColumn.setCellValueFactory(cellData -> {
            Quiz quiz = cellData.getValue();
            int count = quiz.getQuestions() != null ? quiz.getQuestions().size() : 0;
            return new javafx.beans.property.SimpleIntegerProperty(count).asObject();
        });
    }

    private void setupGradesTable() {
        gradeIdColumn.setCellValueFactory(cellData -> {
            QuizResult result = cellData.getValue();
            return new javafx.beans.property.SimpleIntegerProperty(result.hashCode()).asObject();
        });
        gradeQuizColumn.setCellValueFactory(cellData -> {
            QuizResult result = cellData.getValue();
            int id = result.getQuiz() != null ? result.getQuiz().getId() : 0;
            return new javafx.beans.property.SimpleIntegerProperty(id).asObject();
        });
        gradeScoreColumn.setCellValueFactory(cellData -> {
            QuizResult result = cellData.getValue();
            return new javafx.beans.property.SimpleDoubleProperty(result.getScore()).asObject();
        });
        gradeDateColumn.setCellValueFactory(cellData -> {
            // QuizResult doesn't have submittedAt - using placeholder
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
    }

    // ==================== AVAILABLE COURSES TAB ====================

    @FXML
    private void loadAvailableCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            ObservableList<Course> coursesList = FXCollections.observableArrayList(courses);
            availableCoursesTable.setItems(coursesList);
            System.out.println("Loaded " + courses.size() + " available courses");
        } catch (Exception e) {
            showError("Error loading courses: " + e.getMessage());
        }
    }

    @FXML
    private void handleEnrollCourse() {
        Course selected = availableCoursesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a course to enroll!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Enrollment");
        confirm.setHeaderText("Enroll in Course");
        confirm.setContentText("Do you want to enroll in: " + selected.getCourseName() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Enroll using existing method
                enrollmentDAO.enrollStudentInCourse(currentStudent.getId(), selected.getCode());
                showInfo("Successfully enrolled in: " + selected.getCourseName());
                loadMyCourses();

            } catch (Exception e) {
                showError("Error enrolling in course: " + e.getMessage());
            }
        }
    }

    // ==================== MY COURSES TAB ====================

    @FXML
    private void loadMyCourses() {
        try {
            List<Course> courses = enrollmentDAO.getCoursesByStudentId(currentStudent.getId());
            ObservableList<Course> coursesList = FXCollections.observableArrayList(courses);
            myCoursesTable.setItems(coursesList);

            // Update quiz course combo
            quizCourseCombo.setItems(coursesList);

            System.out.println("Loaded " + courses.size() + " enrolled courses");
        } catch (Exception e) {
            showError("Error loading enrolled courses: " + e.getMessage());
        }
    }

    @FXML
    private void handleDropCourse() {
        Course selected = myCoursesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a course to drop!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Drop");
        confirm.setHeaderText("Drop Course");
        confirm.setContentText("Are you sure you want to drop: " + selected.getCourseName() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                enrollmentDAO.removeStudentFromCourse(currentStudent.getId(), selected.getCode());
                showInfo("Successfully dropped: " + selected.getCourseName());
                loadMyCourses();

            } catch (Exception e) {
                showError("Error dropping course: " + e.getMessage());
            }
        }
    }

    // ==================== QUIZZES TAB ====================

    @FXML
    private void handleLoadQuizzes() {
        Course selectedCourse = quizCourseCombo.getValue();
        if (selectedCourse == null) {
            showError("Please select a course first!");
            return;
        }

        try {
            List<Quiz> quizzes = quizService.getQuizzesByCourseCode(selectedCourse.getCode());
            ObservableList<Quiz> quizzesList = FXCollections.observableArrayList(quizzes);
            quizzesTable.setItems(quizzesList);
            System.out.println("Loaded " + quizzes.size() + " quizzes for course: " + selectedCourse.getCourseName());
        } catch (Exception e) {
            showError("Error loading quizzes: " + e.getMessage());
        }
    }

    @FXML
    private void handleTakeQuiz() {
        Quiz selected = quizzesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a quiz to take!");
            return;
        }

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Take Quiz");
        info.setHeaderText("Quiz: " + selected.getTitle());
        info.setContentText("Quiz taking feature requires a separate interface with questions.\n" +
                          "This would be implemented as a new window with question forms.\n\n" +
                          "For now, this is a placeholder.");
        info.showAndWait();
    }

    @FXML
    private void handleViewAllQuizzes() {
        try {
            // Get all quizzes for all enrolled courses
            List<Course> myCourses = enrollmentDAO.getCoursesByStudentId(currentStudent.getId());
            ObservableList<Quiz> allQuizzes = FXCollections.observableArrayList();

            for (Course course : myCourses) {
                List<Quiz> quizzes = quizService.getQuizzesByCourseCode(course.getCode());
                allQuizzes.addAll(quizzes);
            }

            quizzesTable.setItems(allQuizzes);
            System.out.println("Loaded " + allQuizzes.size() + " total quizzes");
        } catch (Exception e) {
            showError("Error loading all quizzes: " + e.getMessage());
        }
    }

    // ==================== MY GRADES TAB ====================

    @FXML
    private void loadMyGrades() {
        try {
            List<QuizResult> results = quizResultService.getResultsByStudentId(currentStudent.getId());
            ObservableList<QuizResult> resultsList = FXCollections.observableArrayList(results);
            gradesTable.setItems(resultsList);

            // Calculate average score
            if (!results.isEmpty()) {
                double total = 0;
                for (QuizResult result : results) {
                    total += result.getScore();
                }
                double average = total / results.size();
                averageScoreLabel.setText(String.format("Average Score: %.2f%%", average));
            } else {
                averageScoreLabel.setText("Average Score: N/A");
            }

            System.out.println("Loaded " + results.size() + " quiz results");
        } catch (Exception e) {
            showError("Error loading grades: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewGradeDetails() {
        QuizResult selected = gradesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a grade to view details!");
            return;
        }

        try {
            Quiz quiz = selected.getQuiz();

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Grade Details");
            info.setHeaderText("Quiz Result Details");
            info.setContentText(
                "Quiz ID: " + (quiz != null ? quiz.getId() : "N/A") + "\n" +
                "Quiz Title: " + (quiz != null ? quiz.getTitle() : "N/A") + "\n" +
                "Score: " + selected.getScore() + "%"
            );
            info.showAndWait();

        } catch (Exception e) {
            showError("Error loading grade details: " + e.getMessage());
        }
    }

    // ==================== UTILITY METHODS ====================

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setHeaderText("Confirm Logout");
        confirm.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            App.showLoginScreen();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

