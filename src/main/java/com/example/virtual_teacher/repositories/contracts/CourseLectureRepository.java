package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.CourseLecture;

public interface CourseLectureRepository {

    void create(CourseLecture courseLecture);

    void update(CourseLecture courseLecture);

    boolean isAddedToCourse(int courseId, int lectureId);

    void delete(int id);

    CourseLecture getByCourseAndLectureId(int courseId, int lectureId);

}
