# JavaFX GUI for University Management System

## Overview
This is a complete JavaFX-based GUI that **replaces the CLI** of the University Management System. It uses the **MVC (Model-View-Controller)** design pattern and reuses all existing service, DAO, and model classes from the CLI version.

## Project Structure

```
src/main/java/com/ums/system/
├── App.java                           # Main JavaFX application launcher
├── Main.java                          # Original CLI (kept for reference)
├── controller/                        # MVC Controllers
│   ├── LoginController.java           # Handles login logic
│   ├── AdminController.java           # Admin panel controller
│   ├── InstructorController.java      # Instructor panel controller
│   └── StudentController.java         # Student panel controller
├── util/
│   └── ServiceLocator.java            # Service dependency management
├── model/                             # Existing model classes (unchanged)
│   ├── User.java
│   ├── Admin.java
│   ├── Instructor.java
│   ├── Student.java
│   ├── Course.java
│   ├── Quiz.java
│   └── ...
├── service/                           # Existing service classes (unchanged)
│   ├── AdminService.java
│   ├── InstructorService.java
│   ├── StudentService.java
│   └── ...
└── dao/                               # Existing DAO classes (unchanged)
    ├── AdminDAOImpl.java
    ├── InstructorDAOImpl.java
    └── ...

src/main/resources/view/               # FXML Views
├── login.fxml                         # Login screen
├── admin.fxml                         # Admin panel
├── instructor.fxml                    # Instructor panel
└── student.fxml                       # Student panel
```

## Architecture

### 1. **ServiceLocator Pattern**
- **File**: `ServiceLocator.java`
- **Purpose**: Centralized initialization and management of all services
- **Replaces**: Static initialization in CLI `Main.java`
- **Benefits**:
  - Single point of service initialization
  - Easy access to services from any controller
  - Clean dependency management

### 2. **MVC Pattern**

#### Model
- **Location**: `com.ums.system.model.*`
- **Status**: **Unchanged** - All existing model classes are reused
- **Classes**: User, Admin, Instructor, Student, Course, Quiz, QuizResult, etc.

#### View (FXML)
- **Location**: `src/main/resources/view/`
- **Files**:
  - `login.fxml` - Login screen UI
  - `admin.fxml` - Admin panel with tabs for courses, users, students, instructors
  - `instructor.fxml` - Instructor panel with courses, students, quizzes, results
  - `student.fxml` - Student panel with enrollment, quizzes, grades

#### Controller
- **Location**: `com.ums.system.controller.*`
- **Files**:
  - `LoginController.java` - Handles authentication
  - `AdminController.java` - Manages admin operations
  - `InstructorController.java` - Manages instructor operations
  - `StudentController.java` - Manages student operations

### 3. **Application Flow**

```
App.java (launch)
    ↓
ServiceLocator initializes all services
    ↓
login.fxml loads with LoginController
    ↓
User enters credentials
    ↓
LoginController authenticates using same logic as CLI
    ↓
Based on role, load appropriate panel:
    - Admin → admin.fxml + AdminController
    - Instructor → instructor.fxml + InstructorController
    - Student → student.fxml + StudentController
    ↓
Controller receives User object and displays data
```

## Features by Role

### 🔑 Login Screen
- Email and password authentication
- Same password verification as CLI (BCrypt)
- Automatic role detection
- Beautiful gradient UI

### 👨‍💼 Admin Panel
**Courses Tab:**
- View all courses
- Create new course
- Delete course
- Refresh list

**Users Tab:**
- View all users (Admin, Instructor, Student)
- Create new user with role selection
- Delete user
- Password hashing on creation

**Students Tab:**
- View all students
- Update student level (Freshman, Sophomore, Junior, Senior)

**Instructors Tab:**
- View all instructors
- View instructor details

### 👨‍🏫 Instructor Panel
**My Courses Tab:**
- View assigned courses
- Auto-populated from database

**All Courses Tab:**
- Browse all available courses

**Students Tab:**
- Select course to view enrolled students
- View student details (ID, name, email, level)

**My Quizzes Tab:**
- Create new quiz for a course
- View all created quizzes
- Delete quiz

**Quiz Results Tab:**
- Select quiz to view results
- View all results for all quizzes
- Score tracking

### 👨‍🎓 Student Panel
**Available Courses Tab:**
- Browse all courses
- Enroll in course with confirmation

**My Courses Tab:**
- View enrolled courses
- Drop course with confirmation

**Quizzes Tab:**
- Select course to view quizzes
- View all quizzes across courses
- Take quiz (placeholder for now)

**My Grades Tab:**
- View all quiz results
- See detailed scores
- Calculate average score automatically

## How to Run

### Prerequisites
- Java 21 or higher
- Maven
- Database (MariaDB/MySQL) configured and running
- `.env` file with database credentials

### Option 1: Using Maven (Recommended)
```bash
# Clean and compile
mvn clean compile

# Run JavaFX application
mvn javafx:run
```

### Option 2: Using IDE (IntelliJ IDEA / Eclipse)
1. Open the project
2. Navigate to `src/main/java/com/ums/system/App.java`
3. Right-click and select "Run 'App.main()'"

### Option 3: Build JAR and Run
```bash
# Build the project
mvn clean package

# Run the JAR (adjust JavaFX module path)
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/UMS-1.0-SNAPSHOT.jar
```

## Database Connection

The GUI uses the **same database connection** as the CLI version:
- Connection managed by `DatabaseConnection.getInstance()`
- Services initialized through `ServiceLocator`
- All CRUD operations use existing DAO implementations

## Key Implementation Details

### 1. **Authentication (LoginController)**
```java
// Same logic as CLI Main.java login()
private User authenticateUser(String email, String password) {
    // Try Admin
    Admin admin = adminService.getAdminByEmail(email);
    if (admin != null && PasswordUtil.verifyPassword(password, admin.getPassword())) {
        return admin;
    }
    // Try Instructor
    // Try Student
    return null;
}
```

### 2. **Role-Based Navigation**
```java
// In LoginController.onLoginSuccess()
switch (user.getRole()) {
    case ADMIN:
        loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
        // Load admin panel
        break;
    case INSTRUCTOR:
        // Load instructor panel
        break;
    case STUDENT:
        // Load student panel
        break;
}
```

### 3. **Service Access**
```java
// In any controller
ServiceLocator serviceLocator = ServiceLocator.getInstance();
courseService = serviceLocator.getCourseService();
studentService = serviceLocator.getStudentService();
// All services available
```

### 4. **Data Binding**
```java
// TableView setup example
courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

// Load data
List<Course> courses = courseService.getAllCourses();
ObservableList<Course> coursesList = FXCollections.observableArrayList(courses);
coursesTable.setItems(coursesList);
```

## Testing the Application

### Test Credentials
Use the same credentials as CLI:
- **Admin**: Look for admin accounts in your database
- **Instructor**: Look for instructor accounts in your database
- **Student**: Look for student accounts in your database

Example (if you have test data):
```
Admin:
Email: admin@ums.edu
Password: <your_admin_password>

Instructor:
Email: instructor@ums.edu
Password: <your_instructor_password>

Student:
Email: student@ums.edu
Password: <your_student_password>
```

## Features Implemented

✅ **Login System**
- Email/password authentication
- Role detection
- Password verification (BCrypt)

✅ **Admin Features**
- Create/Delete courses
- Create/Delete users (all roles)
- View all students
- Update student level
- View all instructors

✅ **Instructor Features**
- View assigned courses
- View enrolled students per course
- Create/Delete quizzes
- View quiz results

✅ **Student Features**
- Browse available courses
- Enroll/Drop courses
- View enrolled courses
- View quizzes per course
- View grades with average calculation

## Advantages Over CLI

1. **User-Friendly**: Intuitive GUI with tabs and tables
2. **Visual Feedback**: Colored buttons, styled tables, confirmation dialogs
3. **Better Data Display**: TableView for structured data
4. **Modern Look**: Professional design with colors per role
5. **No Typing Required**: Click-based interactions
6. **Multi-Tab Interface**: Easy navigation between features
7. **Real-Time Updates**: Refresh buttons to reload data
8. **Confirmation Dialogs**: Safety checks for destructive operations

## Customization

### Change Colors
Edit the `style` attributes in FXML files:
- **Admin**: Blue theme (`#2193b0`)
- **Instructor**: Cyan theme (`#6dd5ed`)
- **Student**: Green theme (`#4caf50`)

### Add New Features
1. Add method to appropriate controller
2. Add button in FXML with `onAction="#methodName"`
3. Method calls existing service
4. Update UI with results

### Modify Tables
Edit TableColumn definitions in FXML and PropertyValueFactory in controller.

## Troubleshooting

### Issue: "Error initializing services"
**Solution**: Check database connection and `.env` file configuration.

### Issue: "FXML not found"
**Solution**: Ensure FXML files are in `src/main/resources/view/` directory.

### Issue: "JavaFX runtime components are missing"
**Solution**: 
```bash
mvn clean install
mvn javafx:run
```

### Issue: "Login fails with correct credentials"
**Solution**: Check password hashing - ensure passwords in database are BCrypt hashed.

## Notes

- **All existing service logic is reused** - No duplication
- **Database schema unchanged** - Uses same tables as CLI
- **Model classes unchanged** - Same POJOs
- **Can run alongside CLI** - Both use same backend
- **Thread-safe** - Service operations run on background threads where needed

## Future Enhancements

Possible additions:
- [ ] Full quiz-taking interface with questions
- [ ] Real-time grade updates
- [ ] Report generation (PDF) from GUI
- [ ] Advanced search and filtering
- [ ] User profile editing
- [ ] Dashboard with statistics
- [ ] Course schedule visualization
- [ ] Email notifications

## Running Both CLI and GUI

The CLI (`Main.java`) and GUI (`App.java`) can coexist:

**Run CLI:**
```bash
java com.ums.system.Main
```

**Run GUI:**
```bash
mvn javafx:run
```

Both use the same database and services!

---

## Summary

This JavaFX GUI provides a **complete replacement** for the CLI with:
- ✅ Full MVC architecture
- ✅ All service classes reused (no changes)
- ✅ Role-based panels (Admin, Instructor, Student)
- ✅ Same authentication logic
- ✅ Professional, modern UI
- ✅ Easy to extend and maintain

**No changes to your existing service, DAO, or model code!** 🎉

