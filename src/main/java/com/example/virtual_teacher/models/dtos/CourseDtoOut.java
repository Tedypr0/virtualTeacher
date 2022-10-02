package com.example.virtual_teacher.models.dtos;

import com.example.virtual_teacher.models.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

public class CourseDtoOut {
    private String title;
    private Topic topic;
    private CourseDescription description;
    private User teacher;
    private int lecturesCount;
    private int commentsCount;

    public CourseDtoOut() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public CourseDescription getDescription() {
        return description;
    }

    public void setDescription(CourseDescription description) {
        this.description = description;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public int getLecturesCount() {
        return lecturesCount;
    }

    public void setLecturesCount(int lecturesCount) {
        this.lecturesCount = lecturesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
