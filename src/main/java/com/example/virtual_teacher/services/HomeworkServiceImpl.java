package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.UnauthorizedOperationException;
import com.example.virtual_teacher.models.Homework;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.repositories.contracts.HomeworkRepository;
import com.example.virtual_teacher.services.contracts.HomeworkService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    private final HomeworkRepository homeworkRepository;

    public HomeworkServiceImpl(HomeworkRepository homeworkRepository) {
        this.homeworkRepository = homeworkRepository;
    }

    @Override
    public Homework getHomeworkById(int homeworkId) {
        return homeworkRepository.getHomeworkById(homeworkId);
    }

    @Override
    public List<Homework> getByUserId(int userId) {
        return homeworkRepository.getByUserId(userId);
    }

    @Override
    public List<Homework> getByTeacherId(User authUser) {
        if(!authUser.isTeacher() && !authUser.isAdmin()){
            throw new UnauthorizedOperationException("User is not authorized");
        }
        return homeworkRepository.getByTeacherId(authUser.getId());
    }

    @Override
    public void create(Homework homework) {
        homeworkRepository.create(homework);
    }

    @Override
    public void update(Homework homework) {
        homeworkRepository.update(homework);
    }

    @Override
    public void softDelete(int id) {
        homeworkRepository.softDelete(id);
    }
}
