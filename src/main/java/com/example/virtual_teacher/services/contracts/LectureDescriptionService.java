package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.LectureDescription;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.LectureDescriptionRepository;

import java.util.List;

public interface LectureDescriptionService {


    String getById(int id);

    LectureDescription create(LectureDescription lectureDescription);

    LectureDescription update(LectureDescription lectureDescription, User authUser);

    LectureDescription delete(int id);
}
