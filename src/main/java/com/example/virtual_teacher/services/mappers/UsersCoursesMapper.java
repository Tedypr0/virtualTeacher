package com.example.virtual_teacher.services.mappers;

import com.example.virtual_teacher.models.Course;
import com.example.virtual_teacher.models.UsersCourses;
import org.springframework.stereotype.Component;

@Component
public class UsersCoursesMapper {

    public UsersCourses studentEnrollToCourse(Course course, int userId){
        UsersCourses usersCourses = new UsersCourses();
        usersCourses.setUserId(userId);
        usersCourses.setCourse(course);
        return usersCourses;
    }

}
