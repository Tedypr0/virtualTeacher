package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.CourseLecture;

public interface CourseLectureService {

    void createOrDeleteCourseLecture(CourseLecture courseLecture);

    boolean isAddedToCourse(int courseId, int lectureId);
}
