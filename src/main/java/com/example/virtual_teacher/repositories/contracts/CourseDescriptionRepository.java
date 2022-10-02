package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.CourseDescription;

import java.util.List;

public interface CourseDescriptionRepository {
    List<CourseDescription> getAll();

    String getByCourseId(int courseId);

    CourseDescription create(CourseDescription courseDescription);

    CourseDescription update(CourseDescription courseDescription);

    CourseDescription delete(int descriptionId);
}
