package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.FilterOptionsCourses;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UsersCourses;

import java.util.List;

public interface CourseService {
    List<Course> getAll();

    List<UsersCourses> getByUserId(int studentId);

    List<Course> getByTeacherId(int teacherId);

    Course getById(int id);

    long courseCount();

    Course getByTitle(String title);

    Course create(User authUser, Course course);

    Course update(User authUser, Course course);

    Course delete(User authUser, int id);

    List<Course> filter(FilterOptionsCourses filterOptionsCourses);
}
