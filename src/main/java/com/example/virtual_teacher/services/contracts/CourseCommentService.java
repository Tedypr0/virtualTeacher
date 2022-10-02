package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.CourseComment;
import com.example.virtual_teacher.models.User;

import java.util.List;

public interface CourseCommentService {
    List<CourseComment> getAll();

    CourseComment getById(int id);

    CourseComment create(CourseComment courseComment);

    CourseComment update(User authUser, CourseComment courseComment);

    CourseComment delete(User authUser, int id);

    List<CourseComment> getByCourseId(int courseId);

    long getCommentsCount();
}
