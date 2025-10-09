CREATE DATABASE UMS_DB;
USE UMS_DB;

-- Main Users table
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       role ENUM('ADMIN', 'STUDENT', 'INSTRUCTOR') NOT NULL
);

-- Students table (extends users)
CREATE TABLE students (
                          user_id INT PRIMARY KEY,
                          level INT,
                          major VARCHAR(100),
                          grade DOUBLE,
                          department ENUM('CS', 'IS', 'IT', 'AI') NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Instructors table (extends users)
CREATE TABLE instructors (
                             user_id INT PRIMARY KEY,
                             department ENUM('CS', 'IS', 'IT', 'AI') NOT NULL,
                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Admins table (extends users)
CREATE TABLE admins (
                        user_id INT PRIMARY KEY,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Courses Table
CREATE TABLE courses (
                         code VARCHAR(10) PRIMARY KEY,
                         course_name VARCHAR(100) NOT NULL,
                         level VARCHAR(20),
                         major VARCHAR(100),
                         lecture_time VARCHAR(50),
                         instructor_id INT,
                         FOREIGN KEY (instructor_id) REFERENCES users(id) ON DELETE SET NULL
);
