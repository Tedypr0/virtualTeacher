package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.LectureDescription;

import java.util.List;

public interface LectureDescriptionRepository {

    List<LectureDescription> getAll();

    String getByLectureId(int lectureId);

    LectureDescription getById(int id);

    LectureDescription create(LectureDescription lectureDescription);

    LectureDescription update(LectureDescription lectureDescription);

    LectureDescription delete(LectureDescription lectureDescription);
}
