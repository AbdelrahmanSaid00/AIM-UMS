<div align="center">

# 🎓 University Management System (UMS)

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.4-blue?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)](https://mariadb.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)

**A comprehensive JavaFX-based University Management System with role-based access control, payment processing, quiz management, and automated PDF report generation.**

[Features](#-features) • [Architecture](#-architecture) • [Installation](#-installation) • [Usage](#-usage) • [Database](#-database-schema) • [Contributing](#-contributing)

</div>

---

## 📋 Table of Contents

- [About](#-about-the-project)
- [Features](#-features)
- [Architecture](#-architecture)
- [Technologies](#-technologies-used)
- [Installation](#-installation)
- [Usage](#-usage)
- [Database Schema](#-database-schema)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [License](#-license)

---

## 📖 About The Project

The **University Management System (UMS)** is a modern, feature-rich JavaFX desktop application designed to streamline university operations. It provides a complete solution for managing courses, students, instructors, quizzes, payments, and enrollments with a secure role-based authentication system and an intuitive graphical user interface.

### 🎯 Key Highlights

- **🖥️ Modern JavaFX GUI**: Sleek, responsive desktop interface with custom CSS styling
- **🔐 Secure Authentication**: BCrypt password encryption and role-based access control
- **💳 Payment System**: Integrated level fee payment processing with transaction tracking
- **📄 PDF Report Generation**: Automated student report generation using iText7
- **📊 Quiz Management**: Comprehensive quiz creation, administration, and result tracking
- **📚 Course Enrollment**: Seamless student course registration and management
- **🎨 Responsive Design**: Custom CSS themes for each user role
- **🏗️ Modular Architecture**: Clean MVC pattern with service layer and DAO implementation

---

## ✨ Features

### 👨‍💼 Admin Features
- ✅ **User Management**
  - Create new users (Admin, Instructor, Student) with encrypted passwords
  - Delete existing users with cascade operations
  - View and search all users in the system
  - Update user information and roles
- ✅ **Course Management**
  - Create new courses with detailed information
  - Delete courses with enrollment handling
  - View all courses with instructor assignments
  - Assign and reassign instructors to courses
- ✅ **System Administration**
  - Full access to all system resources
  - User role assignment and management
  - System-wide statistics and monitoring
- ✅ **Profile Management**
  - View and update admin profile information
  - Change password with encryption

### 👨‍🏫 Instructor Features
- ✅ **Quiz Management**
  - Create quizzes with custom titles and multiple questions
  - Add multiple-choice questions (4 options each)
  - Set correct answers and scoring
  - View all created quizzes with statistics
- ✅ **Course Access**
  - View assigned courses with enrollment numbers
  - Monitor course-specific quizzes
  - Track student performance per course
- ✅ **Student Monitoring**
  - View quiz results and statistics
  - Track student participation and scores
  - Generate performance reports
- ✅ **Profile Management**
  - View and update instructor profile
  - View department information
  - Change password securely

### 👨‍🎓 Student Features
- ✅ **Course Registration**
  - Browse available courses by level and major
  - Register for courses (up to 6 per semester)
  - View enrolled courses with instructor details
  - Unregister from courses when needed
- ✅ **Quiz Participation**
  - Take quizzes for enrolled courses
  - Interactive quiz interface with timer display
  - View quiz results immediately after submission
  - Review detailed quiz answers and explanations
  - Track quiz history and performance
- ✅ **Payment System**
  - View level fee requirements
  - Pay level fees online with multiple payment methods
  - Track payment history with transaction IDs
  - View payment status (Paid/Pending/Failed)
- ✅ **Academic Progress**
  - View grades and average scores
  - Monitor course enrollment status
  - Generate and download PDF student reports
  - Track completed quizzes and performance metrics
- ✅ **Profile Management**
  - View personal information and student details
  - Update profile information
  - Change password with validation
  - View academic level and major

---

## 🏗️ Architecture

This project follows a **Model-View-Controller (MVC)** architecture with a service layer:

```
┌─────────────────────────────────────────────────┐
│     Presentation Layer (JavaFX FXML Views)      │  ← User Interface
│        (Login, Admin, Instructor, Student)       │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│         Controller Layer (JavaFX)               │  ← Event Handling
│  (LoginController, AdminController, etc.)        │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│           Service Layer                         │  ← Business Logic
│  (AdminService, CourseService, PaymentService)  │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│             DAO Layer                           │  ← Data Access
│  (AdminDAO, CourseDAO, PaymentDAO, etc.)        │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│           Model Layer                           │  ← Domain Entities
│  (User, Course, Quiz, Payment, etc.)            │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│         Database (MariaDB)                      │  ← Data Persistence
└─────────────────────────────────────────────────┘
```

### Design Patterns Used

- **MVC (Model-View-Controller)**: Separates UI, business logic, and data
- **DAO (Data Access Object)**: Abstracts database operations
- **Singleton Pattern**: DatabaseConnection and ServiceLocator ensure single instances
- **Service Layer Pattern**: Encapsulates business logic and transaction management
- **Factory Pattern**: User creation with role-specific implementations
- **Observer Pattern**: JavaFX property binding for reactive UI updates

---

## 🛠️ Technologies Used

| Technology | Purpose | Version |
|------------|---------|---------|
| ![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white) | Core Programming Language | 17 |
| ![JavaFX](https://img.shields.io/badge/JavaFX-blue?style=flat&logo=java&logoColor=white) | GUI Framework | 21.0.4 |
| ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apache-maven&logoColor=white) | Build & Dependency Management | 4.0.0 |
| ![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=flat&logo=mariadb&logoColor=white) | Database Management System | Latest |
| ![JDBC](https://img.shields.io/badge/JDBC-007396?style=flat&logo=java&logoColor=white) | Database Connectivity | MariaDB Connector 3.3.0 |
| ![iText7](https://img.shields.io/badge/iText7-red?style=flat) | PDF Report Generation | 7.2.5 |
| ![BCrypt](https://img.shields.io/badge/BCrypt-green?style=flat) | Password Encryption | 0.10.2 |
| ![Dotenv](https://img.shields.io/badge/Dotenv-3.0.0-green?style=flat) | Environment Configuration | 3.0.0 |

---

## 🚀 Installation

### Prerequisites

Before you begin, ensure you have the following installed:

- ☑️ **Java JDK 17** or higher ([Download](https://www.oracle.com/java/technologies/downloads/))
- ☑️ **Apache Maven** ([Download](https://maven.apache.org/download.cgi))
- ☑️ **MariaDB** or **MySQL** ([Download](https://mariadb.org/download/))
- ☑️ **Git** ([Download](https://git-scm.com/downloads))

### Step-by-Step Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/MaroWael/AIM-UMS.git
   cd AIM-UMS
   ```

2. **Set Up the Database**
   ```bash
   # Login to MariaDB
   mysql -u root -p
   
   # Execute the SQL scripts
   source ums.sql
   source payment_schema.sql
   source mock_data.sql
   ```

3. **Configure Database Connection**
   
   Create a `.env` file in the project root:
   ```env
   DB_URL=jdbc:mariadb://localhost:3306/UMS_DB
   DB_USER=your_username
   DB_PASSWORD=your_password
   ```

4. **Build the Project**
   ```bash
   mvn clean install
   ```

5. **Run the Application**
   
   **Option 1: Using Maven JavaFX Plugin (Recommended)**
   ```bash
   mvn javafx:run
   ```
   
   **Option 2: Using the Launcher**
   ```bash
   mvn exec:java -Dexec.mainClass="com.ums.system.Launcher"
   ```
   
   **Option 3: Run the compiled JAR**
   ```bash
   java -jar target/UMS-1.0-SNAPSHOT.jar
   ```

---

## 💻 Usage

### Login System

When you start the application, you'll be greeted with a modern JavaFX login screen with the university logo and a clean interface.

### Default Users

The system comes with pre-configured users for testing:

| Role | Email | Password | Access Level |
|------|-------|----------|--------------|
| Admin | admin@ums.com | admin123 | Full System Access |
| Instructor | instructor@ums.com | inst123 | Course & Quiz Management |
| Student | student@ums.com | stud123 | Course Registration & Quizzes |

### Admin Dashboard

The admin dashboard provides:
- **User Management Tab**: Create, view, and delete users
- **Course Management Tab**: Create, view, and delete courses
- **Statistics Overview**: System-wide metrics and summaries
- **Profile Tab**: View and update admin information

### Instructor Dashboard

The instructor interface includes:
- **My Courses Tab**: View assigned courses and enrolled students
- **Quiz Management Tab**: Create and manage quizzes
- **Results Tab**: View student quiz results and performance
- **Profile Tab**: Update personal information

### Student Dashboard

The student portal features:
- **Available Courses Tab**: Browse and register for courses
- **My Courses Tab**: View enrolled courses and details
- **Quizzes Tab**: Take quizzes for enrolled courses
- **Grades Tab**: View quiz results and average scores
- **Payments Tab**: Pay level fees and view payment history
- **Profile Tab**: View student information and generate reports

### Payment System

Students can:
1. View their current level fee requirement
2. Check payment status (Paid/Pending)
3. Pay using multiple payment methods (Card, Cash, Bank Transfer)
4. View complete payment history with transaction IDs
5. Download payment receipts

**Level Fee Structure:**
- Level 1 (Freshman): 15,000.00 EGP
- Level 2 (Sophomore): 17,500.00 EGP
- Level 3 (Junior): 20,000.00 EGP
- Level 4 (Senior): 22,500.00 EGP

### Quiz System

**For Instructors:**
1. Navigate to "Create Quiz" tab
2. Enter quiz title and select course
3. Add questions with 4 multiple-choice options
4. Mark the correct answer for each question
5. Submit to make quiz available to students

**For Students:**
1. Go to "Quizzes" tab
2. Select a course from enrolled courses
3. Choose an available quiz
4. Click "Take Quiz" to start
5. Answer all questions and submit
6. View results immediately with detailed feedback

### PDF Report Generation

Students can generate comprehensive academic reports:
1. Click "View Profile" in the student dashboard
2. Click "Generate PDF Report" button
3. PDF includes:
   - Personal information and student ID
   - Enrolled courses with instructors
   - Quiz results and scores
   - Payment history and status
   - University branding and logo

Reports are saved in the `reports/` directory with timestamps.

---

## 🗄️ Database Schema

### Entity Relationship Diagram

```
                              ┌──────────────────┐
                              │      USERS       │
                              ├──────────────────┤
                              │ id (PK)          │
                              │ name             │
                              │ email (UNIQUE)   │
                              │ password (HASH)  │
                              │ role (ENUM)      │
                              └────────┬─────────┘
                                       │
                    ┌──────────────────┼──────────────────┐
                    │                  │                  │
                    ▼                  ▼                  ▼
         ┌──────────────────┐ ┌──────────────┐ ┌──────────────┐
         │    STUDENTS      │ │ INSTRUCTORS  │ │    ADMINS    │
         ├──────────────────┤ ├──────────────┤ ├──────────────┤
         │ user_id (PK,FK)  │ │user_id(PK,FK)│ │user_id(PK,FK)│
         │ level            │ │ department   │ └──────────────┘
         │ major            │ └──────┬───────┘
         │ grade            │        │
         │ department       │        │ instructor_id (FK)
         └──┬───────────┬───┘        │
            │           │            ▼
            │           │   ┌──────────────────┐
            │           │   │     COURSES      │
            │           │   ├──────────────────┤
            │           │   │ code (PK)        │
            │           │   │ course_name      │
            │           │   │ level            │
            │           │   │ major            │
            │           │   │ lecture_time     │
            │           │   │ instructor_id(FK)│
            │           │   └────────┬─────────┘
            │           │            │
            │           │   ┌────────┼────────────────┐
            │           │   │                         │
            │           │   │ course_code(FK)         │
            │           │   │                         │
            │           │   ▼                         ▼
            │           │ ┌──────────────────┐   ┌──────────────────┐
            │           │ │ STUDENT_COURSES  │   │     QUIZZES      │
            │           │ │ (Junction Table) │   ├──────────────────┤
            │           │ ├──────────────────┤   │ id (PK)          │
            │           └─┤student_id(PK,FK) │   │ title            │
            │             │course_code(PK,FK)│   │ course_code (FK) │
            │             └──────────────────┘   └────────┬─────────┘
            │                                             │
            │                                             │ quiz_id (FK)
            │                                    ┌────────┼────────┐
            │                                    │                 │
            │                                    ▼                 ▼
            │                           ┌──────────────┐  ┌──────────────────┐
            │                           │  QUESTIONS   │  │  QUIZ_RESULTS    │
            │                           ├──────────────┤  ├──────────────────┤
            │                           │ id (PK)      │  │ id (PK)          │
            │                           │ quiz_id (FK) │  │ student_id (FK)  │
            │                           │ text         │  │ quiz_id (FK)     │
            │                           │ option1      │  │ score            │
            │                           │ option2      │  └────────┬─────────┘
            │                           │ option3      │           │
            │                           │ option4      │           │ result_id (FK)
            │                           │ correct_opt  │           │
            │                           └──────┬───────┘           │
            │                                  │                   │
            │                                  │ question_id (FK)  │
            │                                  │                   │
            │                                  └────────┬──────────┘
            │                                           │
            │                                           ▼
            │                                  ┌──────────────────┐
            │                                  │  QUIZ_ANSWERS    │
            │                                  ├──────────────────┤
            │                                  │ id (PK)          │
            │                                  │ result_id (FK)   │
            │                                  │ question_id (FK) │
            │                                  │ chosen_answer    │
            │                                  └──────────────────┘
            │
            │ student_id (FK)
            │
            ▼
   ┌──────────────────┐
   │     PAYMENTS     │
   ├──────────────────┤
   │ id (PK)          │
   │ student_id (FK)  │
   │ level            │
   │ amount           │
   │ payment_method   │
   │ transaction_id   │
   │ status           │
   │ payment_date     │
   └──────────────────┘
```

### Key Tables

#### 1. **users** - Core user information with encrypted passwords
```sql
- id (Primary Key)
- name, email, password (BCrypt hashed)
- role (ADMIN, STUDENT, INSTRUCTOR)
```

#### 2. **students** - Student-specific data
```sql
- user_id (Foreign Key → users)
- level, major, grade
- department (CS, IS, IT, AI)
```

#### 3. **instructors** - Instructor-specific data
```sql
- user_id (Foreign Key → users)
- department (CS, IS, IT, AI)
```

#### 4. **courses** - Course information
```sql
- code (Primary Key)
- course_name, level, major
- lecture_time
- instructor_id (Foreign Key → users)
```

#### 5. **quizzes** - Quiz information
```sql
- id (Primary Key)
- title
- course_code (Foreign Key → courses)
```

#### 6. **questions** - Quiz questions with multiple-choice options
```sql
- id (Primary Key)
- quiz_id (Foreign Key → quizzes)
- text, option1-4
- correct_option_index (0-3)
```

#### 7. **quiz_results** - Student quiz scores
```sql
- id (Primary Key)
- student_id, quiz_id (Foreign Keys)
- score (number correct)
```

#### 8. **student_courses** - Junction table for student enrollments
```sql
- student_id (Foreign Key → students)
- course_code (Foreign Key → courses)
```

#### 9. **quiz_answers** - Student answers to quiz questions
```sql
- id (Primary Key)
- result_id (Foreign Key → quiz_results)
- question_id (Foreign Key → questions)
- chosen_answer (0-3)
```

#### 10. **payments** - Level fee payment tracking (NEW)
```sql
- id (Primary Key)
- student_id (Foreign Key → students)
- level (1-4)
- amount (decimal)
- payment_method (ENUM)
- transaction_id (UNIQUE)
- status (ENUM: PENDING, COMPLETED, FAILED)
- payment_date (TIMESTAMP)
```

---

## 📁 Project Structure

```
AIM-UMS/
│
├── src/main/java/com/ums/system/
│   ├── App.java                       # JavaFX Application entry point
│   ├── Launcher.java                  # Main launcher class
│   ├── Main.java                      # Legacy console interface
│   ├── PaymentSystemDemo.java         # Payment system demonstration
│   │
│   ├── controller/                    # JavaFX Controllers (MVC)
│   │   ├── LoginController.java       # Login screen controller
│   │   ├── AdminController.java       # Admin dashboard controller
│   │   ├── InstructorController.java  # Instructor dashboard controller
│   │   ├── StudentController.java     # Student dashboard controller
│   │   ├── ProfileController.java     # Profile view controller
│   │   ├── CreateQuizController.java  # Quiz creation controller
│   │   ├── TakeQuizController.java    # Quiz taking controller
│   │   ├── QuizResultDetailController.java  # Quiz results controller
│   │   └── StudentDetailsController.java    # Student details controller
│   │
│   ├── model/                         # Domain models (Entities)
│   │   ├── User.java                  # Base user class
│   │   ├── Admin.java                 # Admin entity
│   │   ├── Instructor.java            # Instructor entity
│   │   ├── Student.java               # Student entity
│   │   ├── Course.java                # Course entity
│   │   ├── Quiz.java                  # Quiz entity
│   │   ├── Question.java              # Question entity
│   │   ├── QuizResult.java            # Quiz result entity
│   │   ├── Payment.java               # Payment entity (NEW)
│   │   ├── PaymentRequest.java        # Payment request DTO (NEW)
│   │   ├── PaymentResult.java         # Payment result DTO (NEW)
│   │   ├── Role.java                  # Role enumeration
│   │   └── Department.java            # Department enumeration
│   │
│   ├── dao/                           # Data Access Objects
│   │   ├── UserDAO.java               # User DAO interface
│   │   ├── AdminDAOImpl.java          # Admin DAO implementation
│   │   ├── InstructorDAOImpl.java     # Instructor DAO implementation
│   │   ├── StudentDAOImpl.java        # Student DAO implementation
│   │   ├── CourseDAO.java             # Course DAO interface
│   │   ├── CourseDAOImpl.java         # Course DAO implementation
│   │   ├── QuizDAO.java               # Quiz DAO interface
│   │   ├── QuizDAOImpl.java           # Quiz DAO implementation
│   │   ├── QuestionDAO.java           # Question DAO interface
│   │   ├── QuestionDAOImpl.java       # Question DAO implementation
│   │   ├── QuizResultDAO.java         # Quiz result DAO interface
│   │   ├── QuizResultDAOImpl.java     # Quiz result DAO implementation
│   │   ├── EnrollmentDAO.java         # Enrollment DAO interface
│   │   ├── EnrollmentDAOImpl.java     # Enrollment DAO implementation
│   │   ├── PaymentDAO.java            # Payment DAO interface (NEW)
│   │   └── PaymentDAOImpl.java        # Payment DAO implementation (NEW)
│   │
│   ├── service/                       # Business logic layer
│   │   ├── AdminService.java          # Admin service interface
│   │   ├── AdminServiceImpl.java      # Admin service implementation
│   │   ├── InstructorService.java     # Instructor service interface
│   │   ├── InstructorServiceImpl.java # Instructor service implementation
│   │   ├── StudentService.java        # Student service interface
│   │   ├── StudentServiceImpl.java    # Student service implementation
│   │   ├── CourseService.java         # Course service interface
│   │   ├── CourseServiceImpl.java     # Course service implementation
│   │   ├── QuizService.java           # Quiz service interface
│   │   ├── QuizServiceImpl.java       # Quiz service implementation
│   │   ├── QuizResultService.java     # Quiz result service interface
│   │   ├── QuizResultServiceImpl.java # Quiz result service implementation
│   │   ├── PaymentService.java        # Payment service interface (NEW)
│   │   ├── PaymentServiceImpl.java    # Payment service implementation (NEW)
│   │   └── QuestionService.java       # Question service interface (NEW)
│   │
│   ├── util/                          # Utility classes
│   │   └── ServiceLocator.java        # Service locator pattern (NEW)
│   │
│   └── utils/                         # Additional utilities
│       ├── DatabaseConnection.java    # Database connection singleton
│       └── ReportGenerator.java       # PDF report generator (NEW)
│
├── src/main/resources/
│   ├── assets/
│   │   └── Ain_Shams_logo.png        # University logo
│   │
│   └── view/                          # JavaFX FXML views
│       ├── login.fxml                 # Login screen
│       ├── login.css                  # Login styles
│       ├── admin.fxml                 # Admin dashboard
│       ├── admin.css                  # Admin styles
│       ├── instructor.fxml            # Instructor dashboard
│       ├── instructor.css             # Instructor styles
│       ├── student.fxml               # Student dashboard
│       ├── student.css                # Student styles
│       ├── profile.fxml               # Profile view
│       ├── profile.css                # Profile styles
│       ├── create_quiz.fxml           # Quiz creation form
│       ├── create_quiz.css            # Quiz creation styles
│       ├── take_quiz.fxml             # Quiz taking interface
│       ├── take_quiz.css              # Quiz taking styles
│       ├── quiz_result_detail.fxml    # Quiz results view
│       ├── quiz_result_detail.css     # Quiz results styles
│       ├── student_details.fxml       # Student details view
│       └── student_details.css        # Student details styles
│
├── reports/                           # Generated PDF reports (NEW)
├── target/                            # Compiled classes
├── pom.xml                            # Maven configuration
├── ums.sql                            # Database schema script
├── payment_schema.sql                 # Payment system schema (NEW)
├── mock_data.sql                      # Sample data script
├── .env                               # Environment variables (create this)
├── FinalUML(AIM).pdf                  # UML documentation
├── UML.drawio                         # UML source file
├── LICENSE                            # Project license
└── README.md                          # Project documentation
```

---

## 🎨 Key Components

### 1. **JavaFX Views** (`resources/view/`)
- FXML files for declarative UI layout
- Custom CSS styling for each role
- Responsive design with TableViews, ComboBoxes, and Forms
- Modern UI with Ain Shams University branding

### 2. **Controllers** (`controller/`)
- Handle user interactions and events
- Bind UI components to data models
- Validate user input
- Navigate between screens

### 3. **Model Layer** (`model/`)
- Contains all entity classes representing database tables
- Implements inheritance hierarchy (User → Admin/Instructor/Student)
- Includes DTOs for data transfer (PaymentRequest, PaymentResult)
- JavaFX properties for reactive UI binding

### 4. **DAO Layer** (`dao/`)
- Handles all database operations with CRUD methods
- Uses PreparedStatements to prevent SQL injection
- Transaction management for complex operations
- Connection pooling support

### 5. **Service Layer** (`service/`)
- Contains business logic and validation
- Acts as intermediary between controllers and DAOs
- Implements transaction boundaries
- Handles error handling and logging

### 6. **Utils** (`utils/`)
- `DatabaseConnection`: Singleton pattern for DB connection management
- `ReportGenerator`: PDF generation using iText7
- `ServiceLocator`: Centralized service management (NEW)
- Loads credentials from `.env` file securely

---

## 🔧 Configuration

### Database Configuration

Edit your `.env` file:

```env
DB_URL=jdbc:mariadb://localhost:3306/UMS_DB
DB_USER=root
DB_PASSWORD=yourpassword
```

### Maven Dependencies

Key dependencies in `pom.xml`:

```xml
<dependencies>
    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.4</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21.0.4</version>
    </dependency>
    
    <!-- MariaDB JDBC Driver -->
    <dependency>
        <groupId>org.mariadb.jdbc</groupId>
        <artifactId>mariadb-java-client</artifactId>
        <version>3.3.0</version>
    </dependency>
    
    <!-- BCrypt for Password Encryption -->
    <dependency>
        <groupId>at.favre.lib</groupId>
        <artifactId>bcrypt</artifactId>
        <version>0.10.2</version>
    </dependency>
    
    <!-- iText7 for PDF Generation -->
    <dependency>
        <groupId>com.itextpdf</groupId>
        <artifactId>itext7-core</artifactId>
        <version>7.2.5</version>
        <type>pom</type>
    </dependency>
    
    <!-- Dotenv for environment variables -->
    <dependency>
        <groupId>io.github.cdimascio</groupId>
        <artifactId>dotenv-java</artifactId>
        <version>3.0.0</version>
    </dependency>
</dependencies>
```

---

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

### How to Contribute

1. **Fork the Project**
2. **Create your Feature Branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Commit your Changes**
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
4. **Push to the Branch**
   ```bash
   git push origin feature/AmazingFeature
   ```
5. **Open a Pull Request**

### Coding Standards

- Follow Java naming conventions and best practices
- Write meaningful commit messages
- Add comments for complex logic
- Update documentation for new features
- Write unit tests where applicable
- Follow the existing code structure and patterns
- Use proper exception handling

---

## 🐛 Known Issues & Future Enhancements

### Current Limitations
- No email verification system for new users
- Limited payment gateway integration (demo mode)
- Quiz timer is display-only (not enforced)
- No real-time notifications

### Planned Features
- 🔔 Real-time notifications system
- 📧 Email verification and notifications
- 💳 Integration with real payment gateways (Stripe, PayPal)
- 📊 Advanced analytics dashboard
- 📅 Academic calendar integration
- 💬 Messaging system between users
- 🔍 Advanced search and filtering
- 📱 Mobile application (Android/iOS)
- 🌐 Web-based interface
- 🔄 Automatic grade calculation
- 📈 Performance analytics and charts
- 🎯 Attendance tracking system
- 📚 Digital library integration

---

## 📄 License

Distributed under the MIT License. See `LICENSE` file for more information.

---

## 👥 Authors

- **Marwan Weal** - [@MaroWael](https://github.com/MaroWael)
- **Islam Ali** - [@IslamAli-0](https://github.com/IslamAli-0)
- **Abdulrahman Saeed** - [@AbdelrahmanSaid00](https://github.com/AbdelrahmanSaid00)

Project Link: [https://github.com/MaroWael/AIM-UMS](https://github.com/MaroWael/AIM-UMS)

---



<div align="center">

### ⭐ If you find this project useful, please consider giving it a star!

**Made with ❤️ and ☕ by the AIM Team**

[⬆ Back to Top](#-university-management-system-ums)

</div>
