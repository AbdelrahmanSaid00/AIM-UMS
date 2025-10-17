package com.ums.system;

import com.ums.system.model.*;
import com.ums.system.service.*;
import com.ums.system.utils.DatabaseConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting UMS demo main...");

        if (args != null && args.length > 0) {
            logger.info("Received args: {}", Arrays.toString(args));
        }

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            if (conn == null) {
                logger.error("No database connection available. Exiting demo.");
                return;
            }

            // Instantiate service layer objects
            AdminService adminService = new AdminServiceImpl(conn);
            InstructorService instructorService = new InstructorServiceImpl(conn);
            CourseService courseService = new CourseServiceImpl(conn);
            StudentService studentService = new StudentServiceImpl(conn);
            QuizService quizService = new QuizServiceImpl(conn);

            // ------------------ Admin flow ------------------
            logger.info("--- Admin flow ---");
            Admin demoAdmin = new Admin(0, "Demo Admin", "admin-demo@example.com", "secret", Role.ADMIN);
            adminService.addAdmin(demoAdmin);

            List<Admin> admins = adminService.getAllAdmins();
            logger.info("Admins in DB: {}", admins);

            Admin fetchedAdmin = admins.stream()
                    .findFirst()
                    .map(a -> adminService.getAdminById(a.getId()))
                    .orElse(null);
            logger.info("Fetched admin: {}", fetchedAdmin);

            // ------------------ Instructor flow (create instructor used by the demo course) ------------------
            logger.info("--- Instructor flow ---");
            String instructorEmail = "instructor-demo@example.com";
            Instructor demoInstructor = new Instructor(0, "Demo Instructor", instructorEmail, "instr-pass", Role.INSTRUCTOR, Department.CS);
            instructorService.addInstructor(demoInstructor);

            List<Instructor> instructors = instructorService.getAllInstructors();
            logger.info("Instructors in DB: {}", instructors);

            // Try to find the created instructor by email to obtain its generated id
            Optional<Instructor> maybeInstructor = instructors.stream()
                    .filter(i -> instructorEmail.equalsIgnoreCase(i.getEmail()))
                    .findFirst();

            Integer instructorId = maybeInstructor.map(Instructor::getId).orElse(null);
            logger.info("Using instructorId={} for demo course", instructorId);

            // ------------------ Course flow ------------------
            logger.info("--- Course flow ---");
            // If instructorId is null, DAO will attempt to insert with 0 and may fail due to FK — we handle that later
            Course demoCourse = new Course("CS101", "Intro to CS", "100", "CS", "Mon 09:00", null, null, instructorId != null ? instructorId : 0);
            courseService.addCourse(demoCourse);

            List<Course> courses = courseService.getAllCourses();
            logger.info("Courses in DB: {}", courses);

            Course fetchedCourse = courseService.getCourseByCode("CS101");
            logger.info("Fetched course: {}", fetchedCourse);

            // ------------------ Student flow ------------------
            logger.info("--- Student flow ---");
            Student demoStudent = new Student(0, "Demo Student", "student-demo@example.com", "password", Role.STUDENT, 1, "CS", null, 0, Department.CS, 0.0);
            studentService.addStudent(demoStudent);

            List<Student> students = studentService.getAllStudents();
            logger.info("Students in DB: {}", students);

            Student fetchedStudent = students.stream()
                    .findFirst()
                    .map(s -> studentService.getStudentById(s.getId()))
                    .orElse(null);
            logger.info("Fetched student: {}", fetchedStudent);

            // ------------------ Quiz & Question flow ------------------
            logger.info("--- Quiz & Question flow ---");
            // Ensure the course exists before creating a quiz referencing it
            if (fetchedCourse == null) {
                logger.warn("Demo course does not exist. Skipping quiz creation to avoid FK constraint violation.");
            } else {
                Question q1 = new Question(0, "What is 2+2?", Arrays.asList("1", "2", "4", "5"), 2);
                Question q2 = new Question(0, "What is the capital of France?", Arrays.asList("Paris", "Berlin", "Rome", "Madrid"), 0);
                Quiz demoQuiz = new Quiz(0, "Sample Quiz", fetchedCourse.getCode(), Arrays.asList(q1, q2));

                quizService.createQuiz(demoQuiz);

                List<Quiz> quizzes = quizService.getAllQuizzes();
                logger.info("Quizzes in DB: {}", quizzes);

                quizzes.stream().findFirst().ifPresent(q -> {
                    Quiz got = quizService.getQuizById(q.getId());
                    logger.info("Fetched quiz: {}", got);
                });
            }

            logger.info("Demo complete. Optionally you can inspect the DB for created rows.");

        } catch (Exception e) {
            logger.error("Unexpected error during demo", e);
        } finally {
            try {
                DatabaseConnection.getInstance().closeConnection();
            } catch (Exception ignore) {
                // ignored
            }
            logger.info("Demo finished.");
        }
    }
}
