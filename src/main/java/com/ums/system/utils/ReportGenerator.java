package com.ums.system.utils;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.ums.system.dao.EnrollmentDAO;
import com.ums.system.model.Course;
import com.ums.system.model.QuizResult;
import com.ums.system.model.Student;
import com.ums.system.service.QuizResultService;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    private EnrollmentDAO enrollmentDAO;
    private QuizResultService quizResultService;

    public ReportGenerator(EnrollmentDAO enrollmentDAO, QuizResultService quizResultService) {
        this.enrollmentDAO = enrollmentDAO;
        this.quizResultService = quizResultService;
    }

    public String generateStudentReport(Student student) throws Exception {

        File reportsDir = new File("reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "reports/Student_Report_" + student.getId() + "_" + timestamp + ".pdf";

        PdfWriter writer = new PdfWriter(filename);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        Paragraph header = new Paragraph("STUDENT ACADEMIC REPORT")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(header);

        Paragraph date = new Paragraph("Report Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(20);
        document.add(date);

        document.add(new Paragraph("Personal Information")
                .setFontSize(16)
                .setBold()
                .setMarginBottom(10));

        Table personalInfoTable = new Table(2);
        personalInfoTable.setWidth(UnitValue.createPercentValue(100));

        addTableRow(personalInfoTable, "Student ID:", String.valueOf(student.getId()), true);
        addTableRow(personalInfoTable, "Name:", student.getName(), false);
        addTableRow(personalInfoTable, "Email:", student.getEmail(), true);
        addTableRow(personalInfoTable, "Level:", String.valueOf(student.getLevel()), false);
        addTableRow(personalInfoTable, "Major:", student.getMajor(), true);
        addTableRow(personalInfoTable, "Department:", student.getDepartmentName().toString(), false);
        addTableRow(personalInfoTable, "Overall Grade:", String.format("%.2f", student.getGrade()) + "%", true);

        document.add(personalInfoTable);
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Registered Courses")
                .setFontSize(16)
                .setBold()
                .setMarginBottom(10));

        List<Course> courses = enrollmentDAO.getCoursesByStudentId(student.getId());

        if (courses.isEmpty()) {
            document.add(new Paragraph("No courses registered yet.")
                    .setItalic()
                    .setFontColor(ColorConstants.GRAY));
        } else {
            Table coursesTable = new Table(new float[]{2, 3, 1, 2, 2});
            coursesTable.setWidth(UnitValue.createPercentValue(100));

            coursesTable.addHeaderCell(createHeaderCell("Course Code"));
            coursesTable.addHeaderCell(createHeaderCell("Course Name"));
            coursesTable.addHeaderCell(createHeaderCell("Level"));
            coursesTable.addHeaderCell(createHeaderCell("Major"));
            coursesTable.addHeaderCell(createHeaderCell("Lecture Time"));

            boolean alternate = false;
            for (Course course : courses) {
                coursesTable.addCell(createDataCell(course.getCode(), alternate));
                coursesTable.addCell(createDataCell(course.getCourseName(), alternate));
                coursesTable.addCell(createDataCell(course.getLevel(), alternate));
                coursesTable.addCell(createDataCell(course.getMajor(), alternate));
                coursesTable.addCell(createDataCell(course.getLectureTime(), alternate));
                alternate = !alternate;
            }

            document.add(coursesTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Course Performance")
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10));

            addCourseGrades(document, student, courses);
        }

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Quiz Results Summary")
                .setFontSize(16)
                .setBold()
                .setMarginBottom(10));

        addQuizResults(document, student);

        document.add(new Paragraph("\n\n"));
        document.add(new Paragraph("This is an official academic report generated by the University Management System.")
                .setFontSize(8)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY));

        document.close();
        return filename;
    }

    private void addTableRow(Table table, String label, String value, boolean alternate) {
        DeviceRgb bgColor = alternate ? new DeviceRgb(240, 240, 240) : new DeviceRgb(255, 255, 255);

        table.addCell(new Cell()
                .add(new Paragraph(label).setBold())
                .setBackgroundColor(bgColor)
                .setPadding(8));

        table.addCell(new Cell()
                .add(new Paragraph(value))
                .setBackgroundColor(bgColor)
                .setPadding(8));
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold())
                .setBackgroundColor(new DeviceRgb(41, 128, 185))
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8);
    }

    private Cell createDataCell(String text, boolean alternate) {
        DeviceRgb bgColor = alternate ? new DeviceRgb(245, 245, 245) : new DeviceRgb(255, 255, 255);
        return new Cell()
                .add(new Paragraph(text))
                .setBackgroundColor(bgColor)
                .setPadding(6);
    }

    private void addQuizResults(Document document, Student student) throws Exception {
        List<QuizResult> results = quizResultService.getResultsByStudentId(student.getId());

        if (results.isEmpty()) {
            document.add(new Paragraph("No quiz results available yet.")
                    .setItalic()
                    .setFontColor(ColorConstants.GRAY));
        } else {
            Table resultsTable = new Table(new float[]{4, 2, 2});
            resultsTable.setWidth(UnitValue.createPercentValue(100));

            resultsTable.addHeaderCell(createHeaderCell("Quiz Title"));
            resultsTable.addHeaderCell(createHeaderCell("Course Code"));
            resultsTable.addHeaderCell(createHeaderCell("Score"));

            int totalScore = 0;
            int totalQuestions = 0;
            boolean alternate = false;

            for (QuizResult result : results) {
                if (result.getQuiz() == null) {
                    continue;
                }

                int quizQuestions = result.getQuiz().getQuestions().size();
                totalScore += result.getScore();
                totalQuestions += quizQuestions;

                resultsTable.addCell(createDataCell(result.getQuiz().getTitle(), alternate));
                resultsTable.addCell(createDataCell(result.getQuiz().getCourseCode(), alternate));
                resultsTable.addCell(createDataCell(result.getScore() + "/" + quizQuestions, alternate));
                alternate = !alternate;
            }

            document.add(resultsTable);

            document.add(new Paragraph("\n"));
            Table summaryTable = new Table(2);
            summaryTable.setWidth(UnitValue.createPercentValue(50));
            addTableRow(summaryTable, "Total Quizzes Taken:", String.valueOf(results.size()), true);
            addTableRow(summaryTable, "Total Points Earned:", totalScore + "/" + totalQuestions, false);
            if (totalQuestions > 0) {
                double percentage = (totalScore * 100.0) / totalQuestions;
                addTableRow(summaryTable, "Average Score:", String.format("%.2f%%", percentage), true);
            }
            document.add(summaryTable);
        }
    }

    private void addCourseGrades(Document document, Student student, List<Course> courses) throws Exception {
        List<QuizResult> allResults = quizResultService.getResultsByStudentId(student.getId());

        Map<String, Integer> courseScores = new HashMap<>();
        Map<String, Integer> courseTotalQuestions = new HashMap<>();

        for (QuizResult result : allResults) {
            if (result.getQuiz() == null) {
                continue;
            }

            String courseCode = result.getQuiz().getCourseCode();
            int quizQuestions = result.getQuiz().getQuestions().size();

            courseScores.put(courseCode, courseScores.getOrDefault(courseCode, 0) + result.getScore());
            courseTotalQuestions.put(courseCode, courseTotalQuestions.getOrDefault(courseCode, 0) + quizQuestions);
        }

        Table gradesTable = new Table(new float[]{3, 2, 2, 2});
        gradesTable.setWidth(UnitValue.createPercentValue(100));

        gradesTable.addHeaderCell(createHeaderCell("Course Name"));
        gradesTable.addHeaderCell(createHeaderCell("Course Code"));
        gradesTable.addHeaderCell(createHeaderCell("Total Points"));
        gradesTable.addHeaderCell(createHeaderCell("Grade"));

        boolean alternate = false;
        for (Course course : courses) {
            String courseCode = course.getCode();
            int score = courseScores.getOrDefault(courseCode, 0);
            int total = courseTotalQuestions.getOrDefault(courseCode, 0);

            String gradeStr = total > 0 ? String.format("%.2f%%", (score * 100.0) / total) : "N/A";
            String pointsStr = total > 0 ? score + "/" + total : "No quizzes yet";

            gradesTable.addCell(createDataCell(course.getCourseName(), alternate));
            gradesTable.addCell(createDataCell(courseCode, alternate));
            gradesTable.addCell(createDataCell(pointsStr, alternate));
            gradesTable.addCell(createDataCell(gradeStr, alternate));
            alternate = !alternate;
        }

        document.add(gradesTable);
    }
}

