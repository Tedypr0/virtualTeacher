package com.example.virtual_teacher.repositories.contracts;


import com.example.virtual_teacher.models.Homework;

import java.util.List;

public interface HomeworkRepository {

    Homework getHomeworkById(int homeworkId);

    List<Homework> getByUserId(int userId);

    List<Homework> getByTeacherId(int teacherId);

    void create(Homework homework);

    void update(Homework homework);

    void softDelete(int id);
}
