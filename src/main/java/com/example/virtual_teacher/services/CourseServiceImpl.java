package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.*;
import com.example.virtual_teacher.repositories.contracts.CourseRepository;
import com.example.virtual_teacher.services.contracts.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    public static final String CREATE_COURSE_ERROR_MESSAGE = "Only owner or admin can create a course!";
    public static final String UPDATE_COURSE_ERROR_MESSAGE = "Only owner or admin can edit or delete a course!";

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    @Override
    public List<Course> getAll() {
        return courseRepository.getAll();
    }

    @Override
    public List<UsersCourses> getByUserId(int studentId) {
        return courseRepository.getByUserId(studentId);
    }

    @Override
    public List<Course> getByTeacherId(int teacherId) {
        return courseRepository.getByTeacherId(teacherId);
    }

    @Override
    public Course getById(int id) {
        Course course = courseRepository.getById(id);
        if (course.getDescription() == null) {
            CourseDescription courseDescription = new CourseDescription();
            courseDescription.setDescription("No Description");
            course.setDescription(courseDescription);
        }
        return course;
    }

    @Override
    public long courseCount() {
        return courseRepository.courseCount();
    }

    @Override
    public Course getByTitle(String title) {
        return courseRepository.getByTitle(title);
    }



    @Override
    public Course create(User authUser, Course course) {
        if (!authUser.isAdmin() && !authUser.isTeacher()) {
            throw new UnauthorizedOperationException(CREATE_COURSE_ERROR_MESSAGE);
        }
        return courseRepository.create(course);
    }

    @Override
    public Course update(User authUser, Course course) {
        if (course.isDeleted()) {
            throw new EntityNotFoundException("id", course.getId());
        }
        if (!authUser.isAdmin() && authUser.getId() != course.getTeacher().getId()) {
            throw new UnauthorizedOperationException(UPDATE_COURSE_ERROR_MESSAGE);
        }
        return courseRepository.update(course);
    }

    @Override
    public Course delete(User authUser, int id) {
        Course courseToDelete = getById(id);
        if (courseToDelete.isDeleted()) {
            throw new EntityNotFoundException("id", courseToDelete.getId());
        }
        if ((!authUser.isAdmin()) && (authUser.getId() != courseToDelete.getTeacher().getId())) {
            throw new UnauthorizedOperationException(UPDATE_COURSE_ERROR_MESSAGE);
        }
        return courseRepository.delete(id);
    }

    @Override
    public List<Course> filter(FilterOptionsCourses filterOptionsCourses) {
        return courseRepository.filter(filterOptionsCourses);
    }
}
