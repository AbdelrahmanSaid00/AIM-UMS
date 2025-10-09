package com.ums.system.service;

import com.ums.system.model.Course;
import java.util.List;

public interface CourseService {
    void addCourse(Course course);
    void updateCourse(Course course);
    void deleteCourse(String code);
    Course getCourseByCode(String code);
    List<Course> getAllCourses();
}
