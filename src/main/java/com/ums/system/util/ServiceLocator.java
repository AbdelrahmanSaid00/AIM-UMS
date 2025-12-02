package com.ums.system.util;

import com.ums.system.dao.*;
import com.ums.system.service.*;
import com.ums.system.utils.DatabaseConnection;
import com.ums.system.utils.ReportGenerator;

import java.sql.Connection;

/**
 * ServiceLocator - Centralized service management
 * Initializes and provides access to all services used in the application
 * This replaces the static initialization in Main.java
 */
public class ServiceLocator {
    
    private static ServiceLocator instance;
    
    // Database connection
    private Connection connection;
    
    // Services
    private AdminService adminService;
    private InstructorService instructorService;
    private CourseService courseService;
    private StudentService studentService;
    private QuizService quizService;
    private QuizResultService quizResultService;
    
    // DAOs
    private EnrollmentDAO enrollmentDAO;
    private QuestionDAO questionDAO;
    
    // Utilities
    private ReportGenerator reportGenerator;
    
    /**
     * Private constructor - initializes all services
     */
    private ServiceLocator() {
        initializeServices();
    }
    
    /**
     * Get singleton instance of ServiceLocator
     */
    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }
    
    /**
     * Initialize all services with database connection
     */
    private void initializeServices() {
        try {
            // Get database connection
            connection = DatabaseConnection.getInstance().getConnection();
            
            if (connection == null) {
                throw new RuntimeException("Failed to establish database connection");
            }
            
            // Initialize services
            adminService = new AdminServiceImpl(connection);
            instructorService = new InstructorServiceImpl(connection);
            courseService = new CourseServiceImpl(connection);
            studentService = new StudentServiceImpl(connection);
            quizService = new QuizServiceImpl(connection);
            quizResultService = new QuizResultServiceImpl(connection);
            
            // Initialize DAOs
            enrollmentDAO = new EnrollmentDAOImpl(connection);
            questionDAO = new QuestionDAOImpl(connection);
            
            // Initialize utilities
            reportGenerator = new ReportGenerator(enrollmentDAO, quizResultService);
            
            System.out.println("ServiceLocator: All services initialized successfully");
            
        } catch (Exception e) {
            System.err.println("ServiceLocator: Error initializing services - " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize services", e);
        }
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Get AdminService
     */
    public AdminService getAdminService() {
        return adminService;
    }
    
    /**
     * Get InstructorService
     */
    public InstructorService getInstructorService() {
        return instructorService;
    }
    
    /**
     * Get CourseService
     */
    public CourseService getCourseService() {
        return courseService;
    }
    
    /**
     * Get StudentService
     */
    public StudentService getStudentService() {
        return studentService;
    }
    
    /**
     * Get QuizService
     */
    public QuizService getQuizService() {
        return quizService;
    }
    
    /**
     * Get QuizResultService
     */
    public QuizResultService getQuizResultService() {
        return quizResultService;
    }
    
    /**
     * Get EnrollmentDAO
     */
    public EnrollmentDAO getEnrollmentDAO() {
        return enrollmentDAO;
    }
    
    /**
     * Get QuestionDAO
     */
    public QuestionDAO getQuestionDAO() {
        return questionDAO;
    }
    
    /**
     * Get ReportGenerator
     */
    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }
    
    /**
     * Clean up resources
     */
    public void shutdown() {
        try {
            if (connection != null) {
                DatabaseConnection.getInstance().closeConnection();
            }
            System.out.println("ServiceLocator: Resources cleaned up successfully");
        } catch (Exception e) {
            System.err.println("ServiceLocator: Error during shutdown - " + e.getMessage());
        }
    }
}

