package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.Homework;
import com.example.virtual_teacher.models.User;

import java.util.List;

public interface HomeworkService {
    Homework getHomeworkById(int homeworkId);

    List<Homework> getByUserId(int userId);

    List<Homework> getByTeacherId(User authUser);

    void create(Homework homework);

    void update(Homework homework);

    void softDelete(int id);
}
