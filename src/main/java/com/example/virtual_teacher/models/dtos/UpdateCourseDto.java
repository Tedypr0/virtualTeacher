package com.example.virtual_teacher.models.dtos;


import com.example.virtual_teacher.models.Topic;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

public class UpdateCourseDto {
    @NotNull
    @Size(min = 5, max = 50, message = "Course title must be between 5 and 50 symbols")
    private String title;

    private String description;

    private String topic;

    private Date startingDate;

    private Date endDate;

    public UpdateCourseDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
