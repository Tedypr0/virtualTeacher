package com.example.virtual_teacher.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Date;

public class NewCourseDto {

    @NotNull
    @Size(min = 5, max = 50, message = "Course title must be between 5 and 50 symbols")
    private String title;

    @NotBlank(message = "Please select a topic")
    private String topic;

    @NotNull(message = "Please select a starting date")
    private Date startingDate;

    @NotNull(message = "Please select an end date")
    private Date endDate;

    private Double avgRating;

    public NewCourseDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
}
