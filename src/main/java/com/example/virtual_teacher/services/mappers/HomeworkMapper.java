package com.example.virtual_teacher.services.mappers;

import com.example.virtual_teacher.models.Homework;
import com.example.virtual_teacher.models.Lecture;
import com.example.virtual_teacher.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomeworkMapper {

    @Autowired
    public HomeworkMapper() {
    }

    public Homework createObjFromParams(User authUser, Lecture lecture, String fileName){
        Homework homework = new Homework();
        homework.setUser(authUser);
        homework.setLecture(lecture);
        homework.setHomeworkName(fileName);
        return homework;
    }
    public Homework updateHomeworkGrade(Homework homework, double grade){
        if(grade<2 || grade>6){
            throw new IllegalArgumentException("grade is not valid");
        }
        homework.setGrade(grade);
        return homework;
    }

}
