package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.CourseDescription;
import com.example.virtual_teacher.models.User;

import java.util.List;

public interface CourseDescriptionService {

    List<CourseDescription> getAll();

    String getById(int id);

    CourseDescription create(CourseDescription courseDescription);

    CourseDescription update(CourseDescription courseDescription, User authUser);

    CourseDescription delete(int id);
}
