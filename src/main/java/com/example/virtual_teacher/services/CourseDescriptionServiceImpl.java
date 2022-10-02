package com.example.virtual_teacher.services;

import com.example.virtual_teacher.models.CourseDescription;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.CourseDescriptionRepository;
import com.example.virtual_teacher.services.contracts.CourseDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseDescriptionServiceImpl implements CourseDescriptionService {

    private final CourseDescriptionRepository courseDescriptionRepository;

    @Autowired
    public CourseDescriptionServiceImpl(CourseDescriptionRepository courseDescriptionRepository) {
        this.courseDescriptionRepository = courseDescriptionRepository;
    }

    @Override
    public List<CourseDescription> getAll() {
        return courseDescriptionRepository.getAll();
    }

    @Override
    public String getById(int id) {
        return courseDescriptionRepository.getByCourseId(id);
    }

    @Override
    public CourseDescription create(CourseDescription courseDescription) {
        return courseDescriptionRepository.create(courseDescription);
    }

    @Override
    public CourseDescription update(CourseDescription courseDescription, User authUser) {
        return courseDescriptionRepository.update(courseDescription);
    }

    @Override
    public CourseDescription delete(int id) {
        return courseDescriptionRepository.delete(id);
    }
}
