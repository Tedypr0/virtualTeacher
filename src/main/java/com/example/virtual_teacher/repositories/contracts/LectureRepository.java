package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Lecture;

import java.util.List;

public interface LectureRepository {

    List<Lecture> getAll();

    Lecture getById(int id);

    List<Lecture> lecturesByCourseId(int id);

    Lecture getByTitle(String title);

    long lectureCount();

    Lecture create(Lecture lecture);

    Lecture update(Lecture lecture);

    Lecture delete(int id);

    List<Lecture> getAllByTeacherId(int id);
}
