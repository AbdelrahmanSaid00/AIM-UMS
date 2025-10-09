package com.ums.system.service;

import com.ums.system.dao.CourseDAOImpl;
import com.ums.system.model.Course;

import java.sql.Connection;
import java.util.List;

public class CourseServiceImpl implements CourseService {

    private final CourseDAOImpl courseDAO;

    public CourseServiceImpl(Connection connection) {
        this.courseDAO = new CourseDAOImpl(connection);
    }

    @Override
    public void addCourse(Course course) {
        courseDAO.insert(course);
    }

    @Override
    public void updateCourse(Course course) {
        courseDAO.update(course);
    }

    @Override
    public void deleteCourse(String code) {
        courseDAO.delete(code);
    }

    @Override
    public Course getCourseByCode(String code) {
        return courseDAO.getByCode(code);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.getAll();
    }
}
