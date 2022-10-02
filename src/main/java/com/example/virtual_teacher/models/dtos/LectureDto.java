package com.example.virtual_teacher.models.dtos;

import com.example.virtual_teacher.models.LectureDescription;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.Video;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LectureDto {

    @NotNull
    @Size(min = 5, max = 50, message = "Lecture title must be between 5 and 50 symbols")
    private String title;
    @Size(max = 1000, message = "Description must be up to 1000 symbols")
    private LectureDescription description;
    private User teacher;
    private Video video;

    public LectureDto() {
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LectureDescription getDescription() {
        return description;
    }

    public void setDescription(LectureDescription description) {
        this.description = description;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
