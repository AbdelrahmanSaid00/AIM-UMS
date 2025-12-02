package com.ums.system.controller;

import com.ums.system.App;
import com.ums.system.model.*;
import com.ums.system.service.*;
import com.ums.system.util.ServiceLocator;
import com.ums.system.utils.PasswordUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

/**
 * AdminController - Handles Admin panel functionality
 * Provides access to all admin features from CLI
 */
public class AdminController {

    @FXML private Label welcomeLabel;
    @FXML private Label userInfoLabel;

    // Courses Tab
    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, Integer> courseIdColumn;
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, String> courseCodeColumn;
    @FXML private TableColumn<Course, Integer> creditsColumn;
    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseCreditsField;

    // Users Tab
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> userNameColumn;
    @FXML private TableColumn<User, String> userEmailColumn;
    @FXML private TableColumn<User, String> userRoleColumn;
    @FXML private ComboBox<String> userTypeCombo;
    @FXML private TextField userNameField;
    @FXML private TextField userEmailField;
    @FXML private PasswordField userPasswordField;

    // Students Tab
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, Integer> studentIdColumn;
    @FXML private TableColumn<Student, String> studentNameColumn;
    @FXML private TableColumn<Student, String> studentEmailColumn;
    @FXML private TableColumn<Student, String> studentLevelColumn;

    // Instructors Tab
    @FXML private TableView<Instructor> instructorsTable;
    @FXML private TableColumn<Instructor, Integer> instructorIdColumn;
    @FXML private TableColumn<Instructor, String> instructorNameColumn;
    @FXML private TableColumn<Instructor, String> instructorEmailColumn;
    @FXML private TableColumn<Instructor, String> instructorDeptColumn;

    // Services
    private Admin currentAdmin;
    private CourseService courseService;
    private AdminService adminService;
    private InstructorService instructorService;
    private StudentService studentService;

    /**
     * Initialize method
     */
    @FXML
    public void initialize() {
        // Get services from ServiceLocator
        ServiceLocator serviceLocator = ServiceLocator.getInstance();
        courseService = serviceLocator.getCourseService();
        adminService = serviceLocator.getAdminService();
        instructorService = serviceLocator.getInstructorService();
        studentService = serviceLocator.getStudentService();

        // Set up tables
        setupCoursesTable();
        setupUsersTable();
        setupStudentsTable();
        setupInstructorsTable();

        // Set up user type combo
        userTypeCombo.setItems(FXCollections.observableArrayList("Admin", "Instructor", "Student"));
        userTypeCombo.setValue("Student");
    }

    /**
     * Set current admin user
     */
    public void setUser(Admin admin) {
        this.currentAdmin = admin;
        welcomeLabel.setText("Welcome, " + admin.getName() + "!");
        userInfoLabel.setText("Role: Admin | ID: " + admin.getId() + " | Email: " + admin.getEmail());

        // Load initial data
        loadAllCourses();
        loadAllUsers();
        loadAllStudents();
        loadAllInstructors();
    }

    /**
     * Setup Courses Table
     */
    private void setupCoursesTable() {
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
    }

    /**
     * Setup Users Table
     */
    private void setupUsersTable() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    /**
     * Setup Students Table
     */
    private void setupStudentsTable() {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
    }

    /**
     * Setup Instructors Table
     */
    private void setupInstructorsTable() {
        instructorIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        instructorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        instructorEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        instructorDeptColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
    }

    // ==================== COURSES TAB ACTIONS ====================

    @FXML
    private void loadAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            ObservableList<Course> coursesList = FXCollections.observableArrayList(courses);
            coursesTable.setItems(coursesList);
            System.out.println("Loaded " + courses.size() + " courses");
        } catch (Exception e) {
            showError("Error loading courses: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateCourse() {
        String name = courseNameField.getText().trim();
        String code = courseCodeField.getText().trim();
        String level = courseCreditsField.getText().trim();

        if (name.isEmpty() || code.isEmpty() || level.isEmpty()) {
            showError("All fields are required!");
            return;
        }

        try {
            // Create course with constructor (code, courseName, level, major, lectureTime, students, quizzes, instructorId)
            Course course = new Course(code, name, level, "Computer Science", "TBD", null, null, 0);

            courseService.addCourse(course);
            showInfo("Course created successfully!");

            // Clear fields and reload
            courseNameField.clear();
            courseCodeField.clear();
            courseCreditsField.clear();
            loadAllCourses();

        } catch (Exception e) {
            showError("Error creating course: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteCourse() {
        Course selected = coursesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a course to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Course");
        confirm.setContentText("Are you sure you want to delete: " + selected.getCourseName() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                courseService.deleteCourse(selected.getCode());
                showInfo("Course deleted successfully!");
                loadAllCourses();
            } catch (Exception e) {
                showError("Error deleting course: " + e.getMessage());
            }
        }
    }

    // ==================== USERS TAB ACTIONS ====================

    @FXML
    private void loadAllUsers() {
        try {
            ObservableList<User> allUsers = FXCollections.observableArrayList();

            // Load all types of users
            List<Admin> admins = adminService.getAllAdmins();
            List<Instructor> instructors = instructorService.getAllInstructors();
            List<Student> students = studentService.getAllStudents();

            allUsers.addAll(admins);
            allUsers.addAll(instructors);
            allUsers.addAll(students);

            usersTable.setItems(allUsers);
            System.out.println("Loaded " + allUsers.size() + " users");
        } catch (Exception e) {
            showError("Error loading users: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateUser() {
        String name = userNameField.getText().trim();
        String email = userEmailField.getText().trim();
        String password = userPasswordField.getText().trim();
        String userType = userTypeCombo.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("All fields are required!");
            return;
        }

        try {
            String hashedPassword = PasswordUtil.hashPassword(password);

            switch (userType) {
                case "Admin":
                    Admin admin = new Admin(0, name, email, hashedPassword, Role.ADMIN);
                    adminService.addAdmin(admin);
                    break;

                case "Instructor":
                    Instructor instructor = new Instructor(0, name, email, hashedPassword, Role.INSTRUCTOR, Department.CS);
                    instructorService.addInstructor(instructor);
                    break;

                case "Student":
                    Student student = new Student(0, name, email, hashedPassword, Role.STUDENT, 1, "Computer Science", null, 0, Department.CS, 0.0);
                    studentService.addStudent(student);
                    break;
            }

            showInfo(userType + " created successfully!");

            // Clear fields and reload
            userNameField.clear();
            userEmailField.clear();
            userPasswordField.clear();
            loadAllUsers();

        } catch (Exception e) {
            showError("Error creating user: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a user to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete User");
        confirm.setContentText("Are you sure you want to delete: " + selected.getName() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Role role = selected.getRole();
                switch (role) {
                    case ADMIN:
                        adminService.deleteAdmin(selected.getId());
                        break;
                    case INSTRUCTOR:
                        instructorService.deleteInstructor(selected.getId());
                        break;
                    case STUDENT:
                        studentService.deleteStudent(selected.getId());
                        break;
                }
                showInfo("User deleted successfully!");
                loadAllUsers();
            } catch (Exception e) {
                showError("Error deleting user: " + e.getMessage());
            }
        }
    }

    // ==================== STUDENTS TAB ACTIONS ====================

    @FXML
    private void loadAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            ObservableList<Student> studentsList = FXCollections.observableArrayList(students);
            studentsTable.setItems(studentsList);
            System.out.println("Loaded " + students.size() + " students");
        } catch (Exception e) {
            showError("Error loading students: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateStudentLevel() {
        Student selected = studentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a student!");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("2", "1", "2", "3", "4");
        dialog.setTitle("Update Student Level");
        dialog.setHeaderText("Update level for: " + selected.getName());
        dialog.setContentText("Choose level (1=Freshman, 2=Sophomore, 3=Junior, 4=Senior):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int newLevel = Integer.parseInt(result.get());
                adminService.updateStudentLevel(selected.getId(), newLevel);
                showInfo("Student level updated successfully!");
                loadAllStudents();
            } catch (Exception e) {
                showError("Error updating student: " + e.getMessage());
            }
        }
    }

    // ==================== INSTRUCTORS TAB ACTIONS ====================

    @FXML
    private void loadAllInstructors() {
        try {
            List<Instructor> instructors = instructorService.getAllInstructors();
            ObservableList<Instructor> instructorsList = FXCollections.observableArrayList(instructors);
            instructorsTable.setItems(instructorsList);
            System.out.println("Loaded " + instructors.size() + " instructors");
        } catch (Exception e) {
            showError("Error loading instructors: " + e.getMessage());
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

