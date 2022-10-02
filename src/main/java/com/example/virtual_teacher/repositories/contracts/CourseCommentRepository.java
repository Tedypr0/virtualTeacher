package com.example.virtual_teacher.repositories.contracts;


import com.example.virtual_teacher.models.CourseComment;

import java.util.List;

public interface CourseCommentRepository {
    List<CourseComment> getAll();

    CourseComment getById(int commentId);

    CourseComment create(CourseComment courseComment);

    CourseComment update(CourseComment courseComment);

    CourseComment delete(int commentId);

    List <CourseComment> getByCourseId(int courseId);

    long getCourseCommentsCount();
}
