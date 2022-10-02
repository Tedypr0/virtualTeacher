package com.example.virtual_teacher.services.contracts;

import com.example.virtual_teacher.models.MotivationalLetter;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UsersCourses;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    List<User> getAll();

    User getById(int id);

    User getByEmail(String email);

    User create(User user);

    User update(User authUser, User user);

    User delete(int id);

    void saveImage(MultipartFile multipartFile, User authUser) throws IOException;

    void createTeacherApplication(int userId, MotivationalLetter motivationalLetter);

    void deleteTeacherApplication(int userId);

    boolean applicationExists(int userId);

    List<User> getAllTeacherApplications();

    boolean isEnrolled(int courseId, int userId);

    void enrollToCourse(UsersCourses usersCourses);

    long getAllTeachers();
    long getAllStudents();
}
