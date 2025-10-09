package com.ums.system;

import com.ums.system.dao.InstructorDAOImpl;
import com.ums.system.dao.StudentDAOImpl;
import com.ums.system.model.*;
import com.ums.system.service.*;
import com.ums.system.utils.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            // ✅ 1️⃣ Connect to DB
            Connection conn = DatabaseConnection.getInstance().getConnection();

            // ✅ 2️⃣ Initialize DAOs and Services
            StudentDAOImpl studentDAO = new StudentDAOImpl(conn);
            InstructorDAOImpl instructorDAO = new InstructorDAOImpl(conn);
            CourseServiceImpl courseService = new CourseServiceImpl(conn);

            // ✅ 3️⃣ Insert Admin
            try (var ps = conn.prepareStatement(
                    "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)"
            )) {
                ps.setString(1, "Admin User");
                ps.setString(2, "admin@ums.edu");
                ps.setString(3, "admin123");
                ps.setString(4, Role.ADMIN.toString());
                ps.executeUpdate();
                System.out.println("✅ Admin added");
            }

            // ✅ 4️⃣ Insert Instructors
            Instructor inst1 = new Instructor("Dr. Omar Farouk", "omar@ums.edu", "omar123",
                    Role.INSTRUCTOR, Department.AI, null);
            Instructor inst2 = new Instructor("Dr. Salma Nabil", "salma@ums.edu", "salma123",
                    Role.INSTRUCTOR, Department.CS, null);
            instructorDAO.insert(inst1);
            instructorDAO.insert(inst2);
            System.out.println("✅ Instructors added");

            // ✅ 5️⃣ Insert Students
            Student st1 = new Student("Marwan Wael", "marwan@student.edu", "12345",
                    Role.STUDENT, 2, "Software Engineering", null, 0,
                    Department.AI, 3.9);
            Student st2 = new Student("Nour Ahmed", "nour@student.edu", "54321",
                    Role.STUDENT, 1, "Computer Science", null, 0,
                    Department.CS, 3.5);
            studentDAO.insert(st1);
            studentDAO.insert(st2);
            System.out.println("✅ Students added");

            // ✅ 6️⃣ Insert Courses
            Course c1 = new Course("AI101", "Intro to AI", "1", "AI", "Mon 9:00 AM",
                    null, null, 2);  // Instructor id 2 (adjust if needed)
            Course c2 = new Course("CS201", "Data Structures", "2", "CS", "Wed 11:00 AM",
                    null, null, 3);
            Course c3 = new Course("CS301", "Operating Systems", "3", "CS", "Thu 1:00 PM",
                    null, null, 3);

            courseService.addCourse(c1);
            courseService.addCourse(c2);
            courseService.addCourse(c3);
            System.out.println("✅ Courses added");

            // ✅ 7️⃣ Display summary
            System.out.println("\n🎯 Database Seeding Completed Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
