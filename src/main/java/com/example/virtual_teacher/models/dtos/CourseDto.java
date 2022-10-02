package com.example.virtual_teacher.models.dtos;

import com.example.virtual_teacher.models.CourseDescription;
import com.example.virtual_teacher.models.Topic;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;

public class CourseDto {

    @NotNull
    @Size(min = 5, max = 50, message = "Course title must be between 5 and 50 symbols")
    private String title;

    @NotNull
    private Topic topic;

    private CourseDescription description;

    public CourseDto() {
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
}
