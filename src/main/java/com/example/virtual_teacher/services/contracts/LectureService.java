package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LectureService {

    List<Lecture> getAll();

    Lecture getById(int id);

    List<Lecture> getByCourseId(int id);

    Lecture create(Lecture lecture, User authUser);
    long lectureCount();

    Lecture update(Lecture lecture, User authUser);

    Lecture delete(User authUser, int id);

    void saveFile(MultipartFile multipartFile, Lecture lecture) throws IOException;

    String saveHomework(MultipartFile multipartFile, Lecture lecture, User authUser) throws IOException;

    List<Lecture> getAllByTeacherId(int id);
}
