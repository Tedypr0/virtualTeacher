package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.MotivationalLetter;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UsersCourses;
import com.example.virtual_teacher.repositories.contracts.UserRepository;
import com.example.virtual_teacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public User create(User user) {
        boolean emailExists = true;
        try{
            getByEmail(user.getEmail());
        }catch(EntityNotFoundException e){
            emailExists = false;
        }

        if(emailExists){
            throw new DuplicateEntityException("User","email",user.getEmail());
        }
        return userRepository.create(user);
    }

    @Override
    public User update(User authUser, User user) {
        return userRepository.update(user);
    }

    @Override
    public User delete(int id) {
        return userRepository.delete(id);
    }

    @Override
    public void saveImage(MultipartFile multipartFile, User authUser) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(Paths.get("").toAbsolutePath()).append("\\src\\main\\resources\\static\\userImages\\");
        byte[] bytes = multipartFile.getBytes();
        Path path = Paths.get(sb + authUser.getEmail()+".jpg");
        Files.write(path,bytes);
    }

    @Override
    public void createTeacherApplication(int userId, MotivationalLetter motivationalLetter) {
            if(!userRepository.applicationExists(userId)) {
                userRepository.createTeacherApplication(userId, motivationalLetter);
            }
    }

    @Override
    public void deleteTeacherApplication(int userId) {
        userRepository.deleteTeacherApplication(userId);
    }

    @Override
    public List<User> getAllTeacherApplications() {
        return userRepository.getAllTeacherApplications();
    }

    @Override
    public boolean isEnrolled(int courseId, int userId) {
        return userRepository.isEnrolled(courseId, userId);
    }

    @Override
    public void enrollToCourse(UsersCourses usersCourses) {
        userRepository.enrollToCourse(usersCourses);
    }

    @Override
    public long getAllTeachers() {
        return userRepository.getAllTeachers();
    }

    @Override
    public long getAllStudents() {
        return userRepository.getAllStudents();
    }

    @Override
    public boolean applicationExists(int userId) {
       return userRepository.applicationExists(userId);
    }
}
