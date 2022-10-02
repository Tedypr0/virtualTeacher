package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.FilterOptionsCourses;
import com.example.virtual_teacher.models.UsersCourses;

import java.util.List;

public interface CourseRepository {

    List<Course> getAll();
    List<UsersCourses> getByUserId(int studentId);

    List<Course> getByTeacherId(int teacherId);

    Course getById(int id);

    long courseCount();

    Course getByTitle(String title);

    Course create(Course course);

    Course update(Course course);

    Course delete(int id);

    List<Course> filter(FilterOptionsCourses filterOptionsCourses);
}
