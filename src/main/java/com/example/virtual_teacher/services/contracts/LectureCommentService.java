package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.LectureComment;
import com.example.virtual_teacher.models.User;

import java.util.List;

public interface LectureCommentService {

    List<LectureComment> getAll(int lectureId);

    LectureComment getById(int id);

    LectureComment create(LectureComment lectureComment);

    LectureComment update(User authUser, LectureComment lectureComment);

    LectureComment delete(User authUser, int id);
}
