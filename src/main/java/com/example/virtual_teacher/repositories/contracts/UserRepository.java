package com.example.virtual_teacher.repositories.contracts;

import com.example.virtual_teacher.models.MotivationalLetter;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UsersCourses;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByEmail(String email);

    User create(User user);

    User update(User user);

    User delete(int id);

    boolean applicationExists(int userId);

    List<User> getAllTeacherApplications();

    void createTeacherApplication(int userId, MotivationalLetter motivationalLetter);

    void deleteTeacherApplication(int userId);
    boolean isEnrolled(int courseId, int userId);

    void enrollToCourse(UsersCourses usersCourses);

    long getAllTeachers();
    long getAllStudents();
}
