package com.example.virtual_teacher.services;

import com.example.virtual_teacher.models.LectureDescription;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.LectureDescriptionRepository;
import com.example.virtual_teacher.services.contracts.LectureDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureDescriptionServiceImpl implements LectureDescriptionService {

    private final LectureDescriptionRepository lectureDescriptionRepository;

    @Autowired
    public LectureDescriptionServiceImpl(LectureDescriptionRepository lectureDescriptionRepository) {
        this.lectureDescriptionRepository = lectureDescriptionRepository;
    }


    @Override
    public String getById(int id) {
        return lectureDescriptionRepository.getByLectureId(id);
    }

    @Override
    public LectureDescription create(LectureDescription lectureDescription) {
        return lectureDescriptionRepository.create(lectureDescription);
    }

    @Override
    public LectureDescription update(LectureDescription lectureDescription, User authUser) {
        return lectureDescriptionRepository.update(lectureDescription);
    }

    @Override
    public LectureDescription delete(int id) {
        LectureDescription lectureDescription = lectureDescriptionRepository.getById(id);
        return lectureDescriptionRepository.delete(lectureDescription);
    }
}
