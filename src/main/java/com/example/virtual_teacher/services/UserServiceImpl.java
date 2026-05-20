package com.example.virtual_teacher.services;

import com.example.virtual_teacher.exceptions.DuplicateEntityException;
import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.MotivationalLetter;
import com.example.virtual_teacher.models.ProfileImage;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UsersCourses;
import com.example.virtual_teacher.repositories.contracts.UserRepository;
import com.example.virtual_teacher.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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

    private static final long MAX_PROFILE_IMAGE_BYTES = 30 * 1024 * 1024;

    @Override
    public void saveImage(MultipartFile multipartFile, User user) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No image file provided.");
        }
        String contentType = multipartFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only image files are allowed.");
        }
        byte[] data = multipartFile.getBytes();
        if (data.length > MAX_PROFILE_IMAGE_BYTES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image must be smaller than 5MB.");
        }
        userRepository.updateProfileImage(user.getId(), data, contentType);
    }

    @Override
    public ProfileImage getProfileImage(int userId) {
        User user = userRepository.getByIdWithProfileImage(userId);
        if (!user.hasProfileImage()) {
            return null;
        }
        return new ProfileImage(user.getProfileImage(), user.getProfileImageContentType());
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
