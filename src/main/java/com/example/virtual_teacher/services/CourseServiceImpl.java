package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.*;
import com.example.virtual_teacher.repositories.contracts.CourseDescriptionRepository;
import com.example.virtual_teacher.repositories.contracts.CourseRepository;
import com.example.virtual_teacher.services.contracts.CourseService;
import com.example.virtual_teacher.services.contracts.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseDescriptionRepository courseDescriptionRepository;
    private final UserService userService;
    public static final String CREATE_COURSE_ERROR_MESSAGE = "Only owner or admin can create a course!";
    public static final String UPDATE_COURSE_ERROR_MESSAGE = "Only owner or admin can edit or delete a course!";

    public CourseServiceImpl(CourseRepository courseRepository,
                             CourseDescriptionRepository courseDescriptionRepository,
                             UserService userService) {
        this.courseRepository = courseRepository;
        this.courseDescriptionRepository = courseDescriptionRepository;
        this.userService = userService;
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
        attachDescription(course);
        return course;
    }

    @Override
    public Course getByPublicId(String publicId) {
        Course course = courseRepository.getByPublicId(publicId);
        attachDescription(course);
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

        CourseDescription description = course.getDescription();
        course.setDescription(null);
        Course updatedCourse = courseRepository.update(course);
        persistDescription(updatedCourse.getId(), description);
        updatedCourse.setDescription(description);
        return updatedCourse;
    }

    private void attachDescription(Course course) {
        CourseDescription description = courseDescriptionRepository.getEntityByCourseId(course.getId());
        if (description == null) {
            description = new CourseDescription();
            description.setDescription("No Description");
        }
        course.setDescription(description);
    }

    private void persistDescription(int courseId, CourseDescription description) {
        if (description == null || description.getDescription() == null
                || "No Description".equals(description.getDescription())) {
            return;
        }

        description.setCourseId(courseId);
        description.setDeleted(false);
        if (description.getId() == 0) {
            courseDescriptionRepository.create(description);
        } else {
            courseDescriptionRepository.update(description);
        }
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
    public void toggleDraftStatus(User authUser, String coursePublicId) {
        Course course = courseRepository.getByPublicId(coursePublicId);
        int courseId = course.getId();
        if (course.isDeleted()) {
            throw new EntityNotFoundException("id", course.getId());
        }
        if (!authUser.isAdmin() && authUser.getId() != course.getTeacher().getId()) {
            throw new UnauthorizedOperationException(UPDATE_COURSE_ERROR_MESSAGE);
        }
        boolean newDraftStatus = !course.isDraft();
        if (newDraftStatus) {
            userService.deleteEnrollmentsByCourseId(courseId);
        }
        courseRepository.updateDraftStatus(courseId, newDraftStatus);
    }

    @Override
    public List<Course> filter(FilterOptionsCourses filterOptionsCourses) {
        return courseRepository.filter(filterOptionsCourses);
    }
}
