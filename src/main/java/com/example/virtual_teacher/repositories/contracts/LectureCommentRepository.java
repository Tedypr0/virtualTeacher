package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.LectureComment;

import java.util.List;

public interface LectureCommentRepository {
    List<LectureComment> getAll(int lectureId);

    LectureComment getById(int commentId);

    LectureComment create(LectureComment lectureComment);

    LectureComment update(LectureComment lectureComment);

    LectureComment delete(int commentId);

    LectureComment getByLectureId(int lectureId);
}
