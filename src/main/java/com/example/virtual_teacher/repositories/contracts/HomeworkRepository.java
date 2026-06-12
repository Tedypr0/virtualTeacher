package com.example.virtual_teacher.repositories.contracts;


import com.example.virtual_teacher.models.Homework;

import java.util.List;

public interface HomeworkRepository {

    Homework getHomeworkById(int homeworkId);

    Homework getByPublicId(String publicId);

    List<Homework> getByUserId(int userId);

    Homework getByUserIdAndLectureId(int userId, int lectureId);

    List<Homework> getByTeacherId(int teacherId);

    void create(Homework homework);

    void update(Homework homework);

    void softDelete(int id);
}
